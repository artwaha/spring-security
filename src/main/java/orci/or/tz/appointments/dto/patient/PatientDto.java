package orci.or.tz.appointments.dto.patient;

import lombok.Data;

@Data
public class PatientDto {
    private String regNo;
    private String fullName;
    private String mobile;
    private String gender;
    private String dob;
    private String billingCategory;
    private String status;
}
