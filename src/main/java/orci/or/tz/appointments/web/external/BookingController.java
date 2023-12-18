package orci.or.tz.appointments.web.external;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import orci.or.tz.appointments.dto.booking.BookingRequestDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.models.Doctor;
import orci.or.tz.appointments.services.BookingService;
import orci.or.tz.appointments.services.DoctorService;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.DateValidator;
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

    private DateValidator dateValidator = new DateValidator();

    @Override
    public ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto)
    throws ResourceNotFoundException, IOException, OperationFailedException {

        //Get the current authenticated user
        String currentAuthenticatedUserName = SecurityContextHolder.getContext().getAuthentication().getName();
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
                                " on this day you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() +
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
                                " on this day you prefer are fully booked. Please choose another day or different specialist.");
                            }
                        } else {
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() +
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
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() +
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
                            throw new OperationFailedException("We're sorry, but " + dr.get().getDoctorName() +
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
                        throw new OperationFailedException("You cannot make an appointment with any of our specialist during the wekends");
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


}


