package orci.or.tz.appointments.dto.booking;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingInayaDto {
    private Long id;
    private LocalDate appointmentDate;
    private String regNo;
    private Long doctorId;
}
