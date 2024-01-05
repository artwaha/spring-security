package orci.or.tz.appointments.dto.booking;

import lombok.Data;

@Data
public class CancelDto {
    private Long bookingId;
    private String cancelationReason;
}
