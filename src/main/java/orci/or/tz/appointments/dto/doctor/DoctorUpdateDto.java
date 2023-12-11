package orci.or.tz.appointments.dto.doctor;

import lombok.Data;

@Data
public class DoctorUpdateDto {
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
}
