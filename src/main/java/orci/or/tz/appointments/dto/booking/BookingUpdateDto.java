package orci.or.tz.appointments.dto.booking;

import java.time.LocalDate;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class BookingUpdateDto {
    @Nullable
    private LocalDate updatedAppointmentDate;

    @Nullable
    private Long doctorId;
}
