package orci.or.tz.appointments.dto.doctor;

import lombok.Data;
import orci.or.tz.appointments.enums.DoctorStatusEnum;


@Data
public class DoctorExternalDto {
    private Long id;
    private String doctorName;
    private DoctorStatusEnum status;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
}

