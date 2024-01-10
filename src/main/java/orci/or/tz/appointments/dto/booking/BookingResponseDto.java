package orci.or.tz.appointments.dto.booking;

import java.time.LocalDate;

import lombok.Data;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Doctor;

@Data
public class BookingResponseDto {
    private Long id;
    private ApplicationUser patient;
    private Doctor doctor;
    private LocalDate appointmentDate;
    private LocalDate createdDate;
    private boolean isPushed;
    private BookingStatusEnum bookingStatus;
    private String cancelationReason;
}
