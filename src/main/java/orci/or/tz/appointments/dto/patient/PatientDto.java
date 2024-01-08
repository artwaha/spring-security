package orci.or.tz.appointments.dto.patient;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PatientDto {
    private String registrationNumber;
    private String fullName;
    private String mobile;
    private String gender;
    private String dob;
    private String billingCategory;
    private String status;
    private boolean confirmed;
    private LocalDateTime resendUntil;
    private Integer resendCount;
}
