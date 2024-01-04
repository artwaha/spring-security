package orci.or.tz.appointments.web.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import orci.or.tz.appointments.dto.booking.BookingCountDto;
import orci.or.tz.appointments.dto.booking.BookingRequestDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.dto.booking.BookingUpdateDto;
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
import orci.or.tz.appointments.web.external.api.BookingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookingController implements BookingApi {

    private final DateValidator dateValidator = new DateValidator();
    @Autowired
    private BookingService bookingService;
    @Autowired
    private InayaService inayaService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private Commons commons;

//    @Autowired
//    private SecurityAuthenticationService securityAuthenticationService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private LoggedUser loggedUser;

    @Override
    public ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto) throws ResourceNotFoundException, IOException, OperationFailedException {

        //Get the current authenticated user
//        String currentAuthenticatedUserName =securityAuthenticationService.getUsername();
//        Optional<ApplicationUser> p = patientService.GetPatientByRegistrationNumber(currentAuthenticatedUserName);
//        if (!p.isPresent()) {
//            throw new ResourceNotFoundException("The user with provided regNo is Not Found");
//        }
        ApplicationUser patient = loggedUser.getInfo();

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
    public ResponseEntity<BookingCountDto> GetTotalCounts() {

        ApplicationUser patient = loggedUser.getInfo();
        BookingCountDto c = new BookingCountDto();
        c.setTotal(bookingService.countAppointmentsForASpecificPatient(patient));
        c.setPending(bookingService.countAppointmentsByBookingStatusAndPatient(BookingStatusEnum.PENDING, patient));
        c.setUpcoming(bookingService.countAppointmentsByBookingStatusAndPatient(BookingStatusEnum.UPCOMING, patient));
        c.setMissed(bookingService.countAppointmentsByBookingStatusAndPatient(BookingStatusEnum.MISSED, patient));
        c.setCancelled(bookingService.countAppointmentsByBookingStatusAndPatient(BookingStatusEnum.CANCELLED, patient));
        c.setAttended(bookingService.countAppointmentsByBookingStatusAndPatient(BookingStatusEnum.ATTENDED, patient));
        return ResponseEntity.ok(c);


    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointments(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        ApplicationUser patient = loggedUser.getInfo();
        List<Booking> appointments = bookingService.GetAllPatientAppointments(patient, pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.countAppointmentsForASpecificPatient(patient);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointmentsByStatus(int page, int size, BookingStatusEnum bookingStatus) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        ApplicationUser patient = loggedUser.getInfo();
        List<Booking> appointments = bookingService.GetAllPatientAppointmentsByBookingStatus( bookingStatus, patient, pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.countAppointmentsByBookingStatusAndPatient(bookingStatus, patient);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetAllAppointmentsByStatusAndDate(int page, int size,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, BookingStatusEnum bookingStatus) {

        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        ApplicationUser patient = loggedUser.getInfo();
        List<Booking> appointments = bookingService.GetAllPatientAppointmentsByDateRangeAndBookingStatus(startDate, endDate, bookingStatus, patient, pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.countAppointmentsByBookingStatusAndDateRange(bookingStatus,startDate,endDate, patient);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);


    }

    // this endpoint Cancels A specific Appointment before the Appointment is reached
    @Override
    public ResponseEntity<BookingResponseDto> CanCelAnAppointment(Long id) throws ResourceNotFoundException, OperationFailedException {
        ApplicationUser patient = loggedUser.getInfo();

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient);
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }

        Booking booking = appointment.get();
        booking.setBookingStatus(BookingStatusEnum.CANCELLED);
        bookingService.SaveAppointment(booking);
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking);
        return ResponseEntity.ok(resp);
    }


    // this endpoint updates the appointment Date and Doctor or One of them
    @Override
    public ResponseEntity<BookingResponseDto> UpdateTheAppointmentDate(Long id, BookingUpdateDto bookingUpdateDto) throws ResourceNotFoundException, OperationFailedException, IOException {

        LocalDate updatedAppointmentDate = bookingUpdateDto.getUpdatedAppointmentDate();
        Long doctorId = bookingUpdateDto.getDoctorId();

        ApplicationUser patient = loggedUser.getInfo();

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient);
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }

        if (appointment.get().getBookingStatus() != BookingStatusEnum.PENDING) {
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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
                                    bookingService.SaveAppointment(booking);
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


    @Override
    public ResponseEntity<BookingResponseDto> GetAnAppointmentById(@PathVariable Long id) throws ResourceNotFoundException, OperationFailedException {
        ApplicationUser patient = loggedUser.getInfo();

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient);
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }

        Booking booking = appointment.get();
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking);
        return ResponseEntity.ok(resp);
    }


}


