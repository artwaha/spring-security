package orci.or.tz.appointments.web.internal;

import orci.or.tz.appointments.dto.booking.BookCountDto;
import orci.or.tz.appointments.dto.booking.BookingCountDto;
import orci.or.tz.appointments.dto.booking.BookingResponseDto;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.services.BookingService;
import orci.or.tz.appointments.services.DoctorService;
import orci.or.tz.appointments.services.NotificationService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.web.internal.api.BookApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookinController implements BookApi {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private Commons commons;

    @Autowired
    private NotificationService notificationService;


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

        List<Booking> appointments = bookingService.GetAllAppointmentsByStatus(bookingStatus,pageRequest);

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
    public ResponseEntity<GenericResponse<List<BookingResponseDto>>> GetAllAppointmentsByStatusAndDate(int page, int size,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate, BookingStatusEnum bookingStatus) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<BookingResponseDto> resp = new ArrayList<>();

        List<Booking> appointments = bookingService.GetAllAppointmentsByStatusAndDate(bookingStatus,startDate,endDate,pageRequest);

        for (Booking appointment : appointments) {
            BookingResponseDto booking = commons.GenerateBookingResponseDto(appointment);
            resp.add(booking);
        }

        GenericResponse<List<BookingResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = bookingService.CountAllAppointmentsByStatusAndDate(bookingStatus,startDate,endDate);
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> ResendToInaya(Long bookingId) throws ResourceNotFoundException {
        Optional<Booking> booking = bookingService.GetAppointmentById(bookingId);

        if(!booking.isPresent()){
            throw new ResourceNotFoundException("Booking with the ID is not Available");
        }

        notificationService.SendBookingToQueue(booking.get());
        BookingResponseDto resp = commons.GenerateBookingResponseDto(booking.get());
        return ResponseEntity.ok(resp);
    }
}
