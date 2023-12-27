package orci.or.tz.appointments.web.external;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import orci.or.tz.appointments.dto.booking.BookingRequestDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.dto.booking.BookingUpdateDto;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.models.Doctor;
import orci.or.tz.appointments.services.BookingService;
import orci.or.tz.appointments.services.DoctorService;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.services.SecurityAuthenticationService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.DateValidator;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.web.external.api.*;

@RestController
public class BookingController implements BookingApi{
    
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

    @Autowired 
    private SecurityAuthenticationService securityAuthenticationService;

    private DateValidator dateValidator = new DateValidator();

    @Override
    public ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto)
    throws ResourceNotFoundException, IOException, OperationFailedException {

        //Get the current authenticated user
        String currentAuthenticatedUserName =securityAuthenticationService.getUsername();
        Optional<ApplicationUser> p = patientService.GetPatientByRegistrationNumber(currentAuthenticatedUserName);
        if (!p.isPresent()) {
            throw new ResourceNotFoundException("The user with provided regNo is Not Found");
        }
        ApplicationUser patient = p.get();

        Long doctorId = bookingRequestDto.getDoctorId();
        LocalDate appontmentDate = bookingRequestDto.getAppointmenDate();
        if (!dateValidator.isHoliday(appontmentDate)) {
            if (!dateValidator.isWeekend(appontmentDate)) {
                // pass some logic to make the appointment
                Optional<Doctor> dr = doctorService.GetDoctorById(doctorId);
                if (!dr.isPresent()) {
                    throw new ResourceNotFoundException("The Specialist with the Provided Id " + doctorId + 
                    " does not exist");
                }
                int doctorInayaId = dr.get().getInayaId();
                String itemsCountNode = inayaService.GetAppointmentsCountToASpecificSpecialist(doctorInayaId, appontmentDate);
                if (itemsCountNode == null) {
                    throw new IOException("The selected Doctor has a NULL value to their bookings count ");
                }

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(itemsCountNode);
                int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt(): 0;
                
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
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + 
                                " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("WEe're sorry, but " + dr.get().getDoctorName() + 
                                " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "TUESDAY":
                        if (dr.get().isTuesday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);
                    
                                bookingService.SaveAppointment(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + 
                                " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("WEe're sorry, but " + dr.get().getDoctorName() + 
                                " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "WEDNESDAY":
                        if (dr.get().isWednesday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);
                    
                                bookingService.SaveAppointment(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + 
                                " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("WEe're sorry, but " + dr.get().getDoctorName() + 
                                " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }

                    case "THURSDAY":
                        if (dr.get().isThursday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);
                    
                                bookingService.SaveAppointment(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + 
                                " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("WEe're sorry, but " + dr.get().getDoctorName() + 
                                " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }
                        
                    case "FRIDAY":
                        if (dr.get().isFriday()) {
                            if (totalItems < 20) {
                                Booking booking = new Booking();
                                booking.setAppointmentDate(appontmentDate);
                                booking.setDoctor(dr.get());
                                booking.setPatient(patient);
                    
                                bookingService.SaveAppointment(booking);
                                BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(booking);
                                return ResponseEntity.ok(bookingResponseDto);
                            } else {
                                throw new OperationFailedException("We're sorry, but appointments with " + dr.get().getDoctorName() + 
                                " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() + 
                                " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                        }
                        


                    default:
                        throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }

            } else {
                throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
            }
        } else {
            String holidayName = dateValidator.getTheHoliDayName(appontmentDate);
            throw new OperationFailedException("We're sorry, but appointments are not available on holidays "+ holidayName +
            ". Pleasechoose another dayy for your appointment");
        }
    }


    @Override
    public ResponseEntity<Integer> GetTotalCountsOfPatientAppointmnetsOrCountsByStatus(BookingStatusEnum bookingStatus) 
    throws ResourceNotFoundException, OperationFailedException {

        String userName = securityAuthenticationService.getUsername();
        if (userName == null) {
            throw new OperationFailedException("UserName is Nul");
        }

        Optional<ApplicationUser> patient = patientService.GetPatientByRegistrationNumber(userName);
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("The User with the userName " + userName + " is Not Found");
        }

        if (bookingStatus == null) {
            Integer totalCounts = bookingService.countAppointmentsForASpecificPatient(patient.get());
            return ResponseEntity.ok(totalCounts);
        } else {
            if (bookingStatus != BookingStatusEnum.UPCOMING) {
                Integer totalCounts = bookingService.countAppointmentsByBookingStatusAndPatient(bookingStatus, patient.get());
                return ResponseEntity.ok(totalCounts);
            }
            List<BookingStatusEnum> bookingStatusList = Arrays.asList(BookingStatusEnum.SUBMITTED, BookingStatusEnum.PENDING);
            Integer totalCounts = bookingService.countAllUpcomingAppointmetsForASpecificPatient(patient.get(), bookingStatusList);
            return ResponseEntity.ok(totalCounts);
            
        }
    }


    @Override
    public ResponseEntity<GenericResponse<List<BookingResponseDto >>> GetAllAppointmentsOrByStatus(
        int page,int size,LocalDate startDate,LocalDate endDate, BookingStatusEnum bookingStatus
    ) throws ResourceNotFoundException, OperationFailedException {

            PageRequest pageRequest = PageRequest.of(page, size);
            List<BookingStatusEnum> bookingStatusList = Arrays.asList(BookingStatusEnum.SUBMITTED, BookingStatusEnum.PENDING);
            List<BookingResponseDto> resp = new ArrayList<>();

            String userName = securityAuthenticationService.getUsername();
            if (userName == null) {
                throw new OperationFailedException("UserName is Nul");
            }

            Optional<ApplicationUser> patient = patientService.GetPatientByRegistrationNumber(userName);
            if (!patient.isPresent()) {
                throw new ResourceNotFoundException("The User with the userName " + userName + " is Not Found");
            }

            if (startDate != null && endDate != null && bookingStatus != null) {

                if (bookingStatus != BookingStatusEnum.UPCOMING) {
                    List<Booking> appointments = bookingService.GetAllPatientAppointmentsByDateRangeAndBookingStatus(startDate, endDate, bookingStatus, patient.get(), pageRequest);
                    for (Booking appointment : appointments) {
                        BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                        resp.add(booking);
                    }

                    GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                    response.setCurrentPage(page);
                    response.setPageSize(size);
                    Integer totalCount = bookingService. countAppointmentsByBookingStatusAndDateRange(
                        bookingStatus, startDate, endDate, patient.get() );

                    response.setTotalItems(totalCount);
                    response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                    response.setData(resp);
                    return ResponseEntity.ok(response);                
                }

                List<Booking> appointments = bookingService.GetAllUpCommingAppointmentsBasedOnDateRange(patient.get(), bookingStatusList, 
                    startDate, endDate, pageRequest);
                    for (Booking appointment : appointments) {
                        BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                        resp.add(booking);
                    }

                    GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                    response.setCurrentPage(page);
                    response.setPageSize(size);
                    Integer totalCount = bookingService.countAllUpcomingAppointmetsForASpecificPatientBasedOndateRange(patient.get(), bookingStatusList, startDate, endDate);
                    response.setTotalItems(totalCount);
                    response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                    response.setData(resp);
                    return ResponseEntity.ok(response);
        
            } else if (startDate != null && endDate != null) {
               List<Booking> appointments = bookingService.GetAllAppointmnentsByDateRange(startDate, endDate,patient.get(), pageRequest);
                for (Booking appointment : appointments) {
                    BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                    resp.add(booking);
                }

                GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                response.setCurrentPage(page);
                response.setPageSize(size);
                Integer totalCount = bookingService.countAppointmnetsByDateRangeAndPatient(startDate, endDate, patient.get());
                response.setTotalItems(totalCount);
                response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                response.setData(resp);
                return ResponseEntity.ok(response);

            } else if (bookingStatus != null) {

                 if (bookingStatus != BookingStatusEnum.UPCOMING) {
                    List<Booking> appointments = bookingService.GetAllPatientAppointmentsByBookingStatus(bookingStatus, patient.get(), pageRequest);
                    for (Booking appointment : appointments) {
                        BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                        resp.add(booking);
                    }

                    GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                    response.setCurrentPage(page);
                    response.setPageSize(size);
                    Integer totalCount = bookingService.countAppointmentsByBookingStatusAndPatient(bookingStatus, patient.get());
                    response.setTotalItems(totalCount);
                    response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                    response.setData(resp);
                    return ResponseEntity.ok(response);
                 }

                List<Booking> appointments = bookingService.GetAllUpcomingAppointments(patient.get(), bookingStatusList, pageRequest);
                for (Booking appointment : appointments) {
                    BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                    resp.add(booking);
                }

                GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                response.setCurrentPage(page);
                response.setPageSize(size);
                Integer totalCount = bookingService.countAllUpcomingAppointmetsForASpecificPatient(patient.get(), bookingStatusList);
                response.setTotalItems(totalCount);
                response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                response.setData(resp);
                return ResponseEntity.ok(response);

            } else {
                List<Booking> appointments = bookingService.GetAllPatientAppointments(patient.get(), pageRequest);
                for (Booking appointment : appointments) {
                    BookingResponseDto  booking = commons.GenerateBookingResponseDto(appointment);
                    resp.add(booking);
                }

                GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
                response.setCurrentPage(page);
                response.setPageSize(size);
                Integer totalCount = bookingService.countAppointmentsByBookingStatusAndPatient(bookingStatus, patient.get());
                response.setTotalItems(totalCount);
                response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                response.setData(resp);
                return ResponseEntity.ok(response);
            }
                
    }

    // this endpoint Cancels A specific Appointment before the Appointment is reached
    @Override
    public  ResponseEntity<BookingResponseDto> CanCelAnAppointment(Long id) 
    throws ResourceNotFoundException, OperationFailedException {
        String userName = securityAuthenticationService.getUsername();
        if (userName == null) {
            throw new OperationFailedException("UserName is Nul");
        }

        Optional<ApplicationUser> patient = patientService.GetPatientByRegistrationNumber(userName);
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("The User with the userName " + userName + " is Not Found");
        }

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient.get());
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
    public  ResponseEntity<BookingResponseDto> UpdateTheAppointmentDate(Long id, BookingUpdateDto bookingUpdateDto) 
    throws ResourceNotFoundException, OperationFailedException, IOException {

        LocalDate updatedAppointmentDate = bookingUpdateDto.getUpdatedAppointmentDate();
        Long doctorId = bookingUpdateDto.getDoctorId();

        String userName = securityAuthenticationService.getUsername();
        if (userName == null) {
            throw new OperationFailedException("UserName is Nul");
        }

        Optional<ApplicationUser> patient = patientService.GetPatientByRegistrationNumber(userName);
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("The User with the userName " + userName + " is Not Found");
        }

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient.get());
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }
        
        if (appointment.get().getBookingStatus() != BookingStatusEnum.PENDING
        || appointment.get().getBookingStatus() != BookingStatusEnum.SUBMITTED) {
            throw new OperationFailedException("Sorry, You Can Only Update The Appointments That You Have Not Attended "
            + " Only And not The Appointments That YoU Missed To Attend And Cancelled Appoints");  
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
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt(): 0;
                    
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays "+ holidayName +
                ". Pleasechoose another dayy for your appointment");
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
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt(): 0;
                    
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + 
                                    " on this dayy you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays "+ holidayName +
                ". Please choose another day for your appointment");
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
                    int totalItems = jsonNode.hasNonNull("totalItems") ? jsonNode.get("totalItems").asInt(): 0;
                    
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
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
                                    throw new OperationFailedException("We're sorry, but appointments with " + doctor.get().getDoctorName() + 
                                    " on this day you prefer are fully booked. Please choose another day or different specialist.");
                                }
                            } else {
                                throw new OperationFailedException("WEe're sorry, but " + doctor.get().getDoctorName() + 
                                    " is not available on " + dayOfWeek + ". Please select another day or a different specialist.");
                            }

                        default:
                            throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                    }

                } else {
                    throw new OperationFailedException("You cannot amke an appointment with any of our specialist during the wekends");
                }
            } else {
                String holidayName = dateValidator.getTheHoliDayName(updatedAppointmentDate);
                throw new OperationFailedException("We're sorry, but appointments are not available on holidays "+ holidayName +
                ". Please choose another day for your appointment");
            }

        } else {
            //then if no data provided return the booking object
            BookingResponseDto bookingResponseDto = commons.GenerateBookingResponseDto(appointment.get());
            return ResponseEntity.ok(bookingResponseDto);
        }


    }
        


    @Override
    public ResponseEntity<BookingResponseDto> GetAnAppointmentById(@PathVariable Long id) throws 
    ResourceNotFoundException, OperationFailedException{
        String userName = securityAuthenticationService.getUsername();
        if (userName == null) {
            throw new OperationFailedException("UserName is Nul");
        }

        Optional<ApplicationUser> patient = patientService.GetPatientByRegistrationNumber(userName);
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("The User with the userName " + userName + " is Not Found");
        }

        Optional<Booking> appointment = bookingService.GetAppointmentByIdForASpecificPatient(id, patient.get());
        if (!appointment.isPresent()) {
            throw new ResourceNotFoundException("The Appointmnet with the provided Id " + id + " is Not Found");
        }

        Booking booking = appointment.get();
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking);
        return ResponseEntity.ok(resp);
    }
   


}


