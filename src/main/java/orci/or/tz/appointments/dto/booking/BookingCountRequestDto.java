package orci.or.tz.appointments.dto.booking;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BookingCountRequestDto {
    private LocalDate bookingDate;
  private Integer doctorId;

}
