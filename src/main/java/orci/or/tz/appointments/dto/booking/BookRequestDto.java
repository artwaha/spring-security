package orci.or.tz.appointments.dto.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookRequestDto {
    private Long doctorId;
    private LocalDate appointmenDate;
    private Long patientId;
}
