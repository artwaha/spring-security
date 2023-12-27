package orci.or.tz.appointments.web.external.api;

import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.booking.*;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;

import java.io.IOException;
import java.time.LocalDate;

import javax.validation.Valid;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RequestMapping("/api/external/bookings")
public interface BookingApi {
    
    @ApiOperation(value = "Create an Appointment ", notes = "Create an Appointment ")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto)
    throws ResourceNotFoundException, IOException, OperationFailedException;
 
    @ApiOperation(value = "Get the total Number of Appointments According to the status or All the Appointments counts for A specific user without status this is just by default to the API",
     notes = "Get the total Number of Appointments According to the status or All the Appointmnets Counts(total) for the specific user e.g this is just by default to the API")
    @GetMapping(value = "/count", produces = "application/json")
    ResponseEntity<Integer> GetTotalCountsOfPatientAppointmnetsOrCountsByStatus(
        @RequestParam(required = false) BookingStatusEnum bookingStatus
    ) throws ResourceNotFoundException, OperationFailedException;


    @ApiOperation(value = "Get Appointments for A specific user According to Status e.g 'pending' or By default the Endpoint returns all the Appointments for the specific logined user", 
    notes ="Get Appointments for A specific user According to Status e.g 'pending' or By default the Endpoint returns all the Appointments for the specific logined user")
    @GetMapping(value = "", produces = "application/json")
   ResponseEntity<GenericResponse<List<BookingResponseDto >>> GetAllAppointmentsOrByStatus(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BookingStatusEnum bookingStatus
    ) throws ResourceNotFoundException, OperationFailedException;

    // An Endpoint to Cancel The Appointments
    @ApiOperation(value = "Cancel An Appointment that was created before the Appointment Day Only",
        notes="Cancel An Appointment that was created before the Appointment Day Only")
    @PostMapping(value = "/{id}", produces =  "application/json")
    ResponseEntity<BookingResponseDto> CanCelAnAppointment(@PathVariable Long id) throws ResourceNotFoundException, OperationFailedException;
   
    //An Endpoint to update the Appointment date
    @ApiOperation(value="This endpoint takes the param of LocalDate, To Update the Appointment date of the Appointment that was already created before the AppointmentDate",
        notes = "This endpoint takes the param of LocalDate, To Update the Appointment date of the Appointment that was already created before the AppointmentDate")
    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<BookingResponseDto> UpdateTheAppointmentDate(@PathVariable Long id, 
        @RequestBody BookingUpdateDto bookingUpdateDto) throws ResourceNotFoundException, 
        OperationFailedException, IOException;

    @ApiOperation(value = "Get An Appoint for the A specific logined Patient By Id", notes = "Get An Appoint for the A specific logined Patient By Id")
    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<BookingResponseDto> GetAnAppointmentById(@PathVariable Long id) throws ResourceNotFoundException, OperationFailedException;
    
}
