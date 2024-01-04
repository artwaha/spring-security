package orci.or.tz.appointments.web.external.api;

import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.booking.BookingCountDto;
import orci.or.tz.appointments.dto.booking.BookingRequestDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.dto.booking.BookingUpdateDto;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@RequestMapping("/api/external/bookings")
public interface BookingApi {

    @ApiOperation(value = "Create an Appointment ", notes = "Create an Appointment ")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    ResponseEntity<BookingResponseDto> createAppointment(@Valid @RequestBody BookingRequestDto bookingRequestDto)
            throws ResourceNotFoundException, IOException, OperationFailedException;

    @ApiOperation(value = "Get Counts", notes = "Get Counts")
    @GetMapping(value = "/count", produces = "application/json")
    ResponseEntity<BookingCountDto> GetTotalCounts();


    @ApiOperation(value = "Get Appointments By Status and Date",
            notes = "Get Appointments By Status and Date")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetAllAppointmentsByStatusAndDate(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BookingStatusEnum bookingStatus
    ) ;

    // An Endpoint to Cancel The Appointments
    @ApiOperation(value = "Cancel An Appointment that was created before the Appointment Day Only",
            notes = "Cancel An Appointment that was created before the Appointment Day Only")
    @PostMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<BookingResponseDto> CanCelAnAppointment(@PathVariable Long id) throws ResourceNotFoundException, OperationFailedException;

    //An Endpoint to update the Appointment date
    @ApiOperation(value = "This endpoint takes the param of LocalDate, To Update the Appointment date of the Appointment that was already created before the AppointmentDate",
            notes = "This endpoint takes the param of LocalDate, To Update the Appointment date of the Appointment that was already created before the AppointmentDate")
    @PutMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<BookingResponseDto> UpdateTheAppointmentDate(@PathVariable Long id,
                                                                @RequestBody BookingUpdateDto bookingUpdateDto) throws ResourceNotFoundException,
            OperationFailedException, IOException;

    @ApiOperation(value = "Get An Appoint for the A specific logined Patient By Id", notes = "Get An Appoint for the A specific logined Patient By Id")
    @GetMapping(value = "/{id}", produces = "application/json")
    ResponseEntity<BookingResponseDto> GetAnAppointmentById(@PathVariable Long id) throws ResourceNotFoundException, OperationFailedException;

}
