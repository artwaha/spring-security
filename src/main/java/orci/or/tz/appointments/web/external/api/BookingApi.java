package orci.or.tz.appointments.web.external.api;

import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.booking.*;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/external/bookings")
public interface BookingApi {
    
    @ApiOperation(value = "Create aan Appointment ", notes = "Create an Appointment ")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto)
    throws ResourceNotFoundException, IOException, OperationFailedException;

}
