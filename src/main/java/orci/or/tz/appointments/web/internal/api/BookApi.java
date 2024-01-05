package orci.or.tz.appointments.web.internal.api;


import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.booking.BookCountDto;
import orci.or.tz.appointments.dto.booking.BookRequestDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.dto.booking.CancelDto;
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

@RequestMapping("/api/internal/bookings")
public interface BookApi {

    @ApiOperation(value = "Get Counts", notes = "Get Counts")
    @GetMapping(value = "/count", produces = "application/json")
    ResponseEntity<BookCountDto> GetTotalCounts();

    @ApiOperation(value = "Get All Appointments",
            notes = "Get All Appointments")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointments(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    );

    @ApiOperation(value = "Get Appointments By Status",
            notes = "Get Appointments By Status")
    @GetMapping(value = "/status", produces = "application/json")
    ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetUserAppointmentsByStatus(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) BookingStatusEnum bookingStatus
    );

    @ApiOperation(value = "Get Appointments By Status and Date",
            notes = "Get Appointments By Status and Date")
    @GetMapping(value = "status/date", produces = "application/json")
    ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetAllAppointmentsByStatusAndDate(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) BookingStatusEnum bookingStatus
    );


    @ApiOperation(value = "Resend Booking To Inaya", notes = "Resend Booking To Inaya")
    @RequestMapping(value = "/resend", method = RequestMethod.POST, produces = "application/json")
    ResponseEntity<?> ResendToInaya(@RequestParam Long bookingId) throws ResourceNotFoundException;

    @ApiOperation(value = "Create an Appointment ", notes = "Create an Appointment ")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    ResponseEntity<BookingResponseDto> CreateAppointment(@Valid @RequestBody BookRequestDto bookingRequestDto)
            throws ResourceNotFoundException, IOException, OperationFailedException;


    @ApiOperation(value = "Cancel Appointment", notes = "Cancel Appointment")
    @PostMapping(value = "/cancel", consumes = "application/json", produces = "application/json")
    ResponseEntity<BookingResponseDto> CanCelAnAppointment(@RequestBody CancelDto request) throws ResourceNotFoundException, OperationFailedException;


}
