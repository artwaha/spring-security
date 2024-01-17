package orci.or.tz.appointments.web.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import orci.or.tz.appointments.dto.booking.*;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.models.Doctor;
import orci.or.tz.appointments.services.*;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.DateValidator;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.web.internal.api.BookApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookinController implements BookApi {

    private final DateValidator dateValidator = new DateValidator();

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private Commons commons;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private InayaService inayaService;


    @Override
    public ResponseEntity<BookCountDto> GetTotalCounts() {
        BookCountDto c = new BookCountDto();
        c.setTotal(bookingService.CountAllAppointments());
        c.setPending(bookingService.CountAllAppointmentsByStatus(BookingStatusEnum.PENDING));
        c.setUpcoming(bookingService.CountAllAppointmentsByStatus(BookingStatusEnum.UPCOMING));
        c.setMissed(bookingService.CountAllAppointmentsByStatus(BookingStatusEnum.MISSED));
        c.setCancelled(bookingService.CountAllAppointmentsByStatus(BookingStatusEnum.CANCELLED));
        c.setAttended(bookingService.CountAllAppointmentsByStatus(BookingStatusEnum.ATTENDED));
        c.setTotalDoctors(doctorService.countTotalItems());
        return ResponseEntity.ok(c);
    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        List<Booking> appointments = bookingService.GetAllAppointments(pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.CountAllAppointments();
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointmentsByStatus(int page, int size, BookingStatusEnum bookingStatus) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        List<Booking> appointments = bookingService.GetAllAppointmentsByStatus(bookingStatus, pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.CountAllAppointmentsByStatus(bookingStatus);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetAllAppointmentsByStatusAndDate(int page, int size, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, BookingStatusEnum bookingStatus) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        List<Booking> appointments = bookingService.GetAllAppointmentsByStatusAndDate(bookingStatus, startDate, endDate, pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.CountAllAppointmentsByStatusAndDate(bookingStatus, startDate, endDate);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> ResendToInaya(Long bookingId) throws ResourceNotFoundException {
        Optional<Booking> booking = bookingService.GetAppointmentById(bookingId);

        if (!booking.isPresent()) {
            throw new ResourceNotFoundException("Booking with the ID is not Available");
        }

        notificationService.SendBookingToQueue(booking.get());
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking.get());
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> ResendBookingCancelation(Long bookingId) throws ResourceNotFoundException {
        Optional<Booking> booking = bookingService.GetAppointmentById(bookingId);

        if (!booking.isPresent()) {
            throw new ResourceNotFoundException("Booking with the ID is not Available");
        }

        notificationService.SendBookingCancelationToQueue(booking.get());
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking.get());
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<?> ResendBookingUpdate(Long bookingId) throws ResourceNotFoundException {
        Optional<Booking> booking = bookingService.GetAppointmentById(bookingId);

        if (!booking.isPresent()) {
            throw new ResourceNotFoundException("Booking with the ID is not Available");
        }

        notificationService.SendBookingUpdateToQueue(booking.get());
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking.get());
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<BookingResponseDto> CreateAppointment(BookRequestDto bookingRequestDto) throws ResourceNotFoundException, IOException, OperationFailedException {
        Optional<ApplicationUser> pat = patientService.GetPatientById(bookingRequestDto.getPatientId());

        if (!pat.isPresent()) {
            throw new ResourceNotFoundException("Patient with the provided ID does not exist");
        }

        ApplicationUser patient = pat.get();

        Long doctorId = bookingRequestDto.getDoctorId();
        LocalDate appontmentDate = bookingRequestDto.getAppointmenDate();
        if (!dateValidator.isHoliday(appontmentDate)) {
            if (!dateValidator.isWeekend(appontmentDate)) {
                // pass some logic to make the appointment
                Optional<Doctor> dr = doctorService.GetDoctorById(doctorId);
                if (!dr.isPresent()) {
                    throw new ResourceNotFoundException("The Specialist with the Provided Id " + doctorId + " does not exist");
                }
                int doctorInayaId = dr.get().getInayaId();
                String itemsCountNode = inayaService.GetAppointmentsCountToASpecificSpecialist(doctorInayaId, appontmentDate);
                if (itemsCountNode == null) {
                    throw new IOException("The selected Doctor has a NULL value to their bookings count ");
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(itemsCountNode);
                int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt() : 0;

                //validate the working day of the chosen specialist
                String dayOfWeek = DateValidator.getDayOfWeek(appontmentDate);
                switch (dayOfWeek.toUpperCase()) {
                    case "MONDAY":
                        if (dr.get().isMonday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);

                                bookingService.SaveAppointment(booking);
                                notificationService.SendBookingToQueue(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "TUESDAY":
                        if (dr.get().isTuesday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);

                                bookingService.SaveAppointment(booking);
                                notificationService.SendBookingToQueue(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "WEDNESDAY":
                        if (dr.get().isWednesday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);

                                bookingService.SaveAppointment(booking);
                                notificationService.SendBookingToQueue(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "THURSDAY":
                        if (dr.get().isThursday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);

                                bookingService.SaveAppointment(booking);
                                notificationService.SendBookingToQueue(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "FRIDAY":
                        if (dr.get().isFriday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);

                                bookingService.SaveAppointment(booking);
                                notificationService.SendBookingToQueue(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }


                    default:
                        throw new OperationFailedException("You cannot make an appointment with any of our specialist during the wekends");
                }

            } else {
                throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
            }
        } else {
            String holidayName = dateValidator.getTheHoliDayName(appontmentDate);
            throw new OperationFailedException("We're sorry, but appointments are not available on holidays " + holidayName + ". Pleasechoose another dayy for your appointment");
        }
    }

    @Override
    public ResponseEntity<BookingResponseDto> CanCelAnAppointment(CancelDto request) throws ResourceNotFoundException, OperationFailedException {

        Optional<Booking> appointment = bookingService.GetAppointmentById(request.getBookingId());
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + request.getBookingId() + " is Not Found");
        }


        // Update Booking as Cancelled
        Booking booking = appointment.get();
        booking.setBookingStatus(BookingStatusEnum.PENDING_CANCELATION);
        booking.setCancelationReason(request.getCancelationReason());
        bookingService.SaveAppointment(booking);

        // Trigger Inaya Cancelation
        notificationService.SendBookingCancelationToQueue(booking);


        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking);
        return ResponseEntity.ok(resp);
    }

    @Override
    public ResponseEntity<BookingResponseDto> UpdateTheAppointmentDate(Long id, BookingUpdateDto bookingUpdateDto) throws ResourceNotFoundException, OperationFailedException, IOException {
        LocalDate updatedAppointmentDate = bookingUpdateDto.getUpdatedAppointmentDate();
        Long doctorId = bookingUpdateDto.getDoctorId();


        Optional<Booking> appointment = bookingService.GetAppointmentById(id);

        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }

        if (appointment.get().getBookingStatus() == BookingStatusEnum.ATTENDED || appointment.get().getBookingStatus() == BookingStatusEnum.MISSED || appointment.get().getBookingStatus() == BookingStatusEnum.PENDING || appointment.get().getBookingStatus() == BookingStatusEnum.CANCELLED ) {
            throw new OperationFailedException("Sorry, You Can Only Update The Appointments That You Have Not Attended " + " Only And not The Appointments That YoU Missed To Attend And Cancelled Appoints");
        }

        if (updatedAppointmentDate != null && doctorId != null) {
            // change both the doctor Id and date
            if (updatedAppointmentDate.isBefore(LocalDate.now())) {
                throw new OperationFailedException("You cannot Place an Appointment For Past Dates, But Only for The Current And Future Dates");
            }

            Optional<Doctor> doctor = doctorService.GetDoctorById(doctorId);
            if (!doctor.isPresent()) {
                throw new ResourceNotFoundException("The Specialist with provided Id " + doctorId + " is Not Found");
            }

            if (!dateValidator.isHoliday(updatedAppointmentDate)) {
                if (!dateValidator.isWeekend(updatedAppointmentDate)) {
                    // pass some logic to make the appointment

                    int doctorInayaId = doctor.get().getInayaId();
                    String itemsCountNode = inayaService.GetAppointmentsCountToASpecificSpecialist(doctorInayaId, updatedAppointmentDate);
                    if (itemsCountNode == null) {
                        throw new IOException("The selected Doctor has a NULL value to their bookings count ");
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(itemsCountNode);
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt() : 0;

                    //validate the working day of the chosen specialist
                    String dayOfWeek = DateValidator.getDayOfWeek(updatedAppointmentDate);
                    switch (dayOfWeek.toUpperCase()) {
                        case "MONDAY":
                            if (doctor.get().isMonday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "TUESDAY":
                            if (doctor.get().isTuesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "WEDNESDAY":
                            if (doctor.get().isWednesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "THURSDAY":
                            if (doctor.get().isThursday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "FRIDAY":
                            if (doctor.get().isFriday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays " + holidayName + ". Pleasechoose another dayy for your appointment");
            }

        } else if (updatedAppointmentDate != null) {
            // modify the date only
            if (!dateValidator.isHoliday(updatedAppointmentDate)) {
                if (!dateValidator.isWeekend(updatedAppointmentDate)) {
                    // pass some logic to make the appointment

                    int doctorInayaId = appointment.get().getDoctor().getInayaId();
                    Doctor doctor = appointment.get().getDoctor();
                    String itemsCountNode = inayaService.GetAppointmentsCountToASpecificSpecialist(doctorInayaId, updatedAppointmentDate);
                    if (itemsCountNode == null) {
                        throw new IOException("The selected Doctor has a NULL value to their bookings count ");
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(itemsCountNode);
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt() : 0;

                    //validate the working day of the chosen specialist
                    String dayOfWeek = DateValidator.getDayOfWeek(updatedAppointmentDate);
                    switch (dayOfWeek.toUpperCase()) {
                        case "MONDAY":
                            if (doctor.isMonday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "TUESDAY":
                            if (doctor.isTuesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "WEDNESDAY":
                            if (doctor.isWednesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "THURSDAY":
                            if (doctor.isThursday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "FRIDAY":
                            if (doctor.isFriday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setAppointmentDate(updatedAppointmentDate);
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays " + holidayName + ". Please choose another day for your appointment");
            }

        } else if (doctorId != null) {
            // update the doctor Only
            LocalDate appointmentDate = appointment.get().getAppointmentDate();
            Optional<Doctor> doctor = doctorService.GetDoctorById(doctorId);
            if (!doctor.isPresent()) {
                throw new ResourceNotFoundException("The Specialist with provided Id " + doctorId + " is Not Found");
            }

            if (!dateValidator.isHoliday(appointmentDate)) {
                if (!dateValidator.isWeekend(appointmentDate)) {
                    // pass some logic to make the appointment

                    int doctorInayaId = doctor.get().getInayaId();
                    String itemsCountNode = inayaService.GetAppointmentsCountToASpecificSpecialist(doctorInayaId, appointmentDate);
                    if (itemsCountNode == null) {
                        throw new IOException("The selected Doctor has a NULL value to their bookings count ");
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(itemsCountNode);
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt() : 0;

                    //validate the working day of the chosen specialist
                    String dayOfWeek = DateValidator.getDayOfWeek(appointmentDate);
                    switch (dayOfWeek.toUpperCase()) {
                        case "MONDAY":
                            if (doctor.get().isMonday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "TUESDAY":
                            if (doctor.get().isTuesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "WEDNESDAY":
                            if (doctor.get().isWednesday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "THURSDAY":
                            if (doctor.get().isThursday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        case "FRIDAY":
                            if (doctor.get().isFriday()) {
                                if (totalItems < 20) {
                                    Booking booking = appointment.get();
                                    booking.setDoctor(doctor.get());
                                    booking.setPushed(false);
                                    booking.setBookingStatus(BookingStatusEnum.PENDING_UPDATION);
                                    bookingService.SaveAppointment(booking);

                                    //Update Queue
                                    notificationService.SendBookingUpdateToQueue(booking);
                                    BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                    return ResponseEntity.ok(bookingResponseDto);
                                } else {
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays " + holidayName + ". Please choose another day for your appointment");
            }

        } else {
            //then if no data provided return the booking object
            BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(appointment.get());
            return ResponseEntity.ok(bookingResponseDto);
        }
    }


}
