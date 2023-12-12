package orci.or.tz.appointments.dto.booking;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookingRequestDto {
    private Long doctorId;
    private LocalDate appointmenDate;
}
