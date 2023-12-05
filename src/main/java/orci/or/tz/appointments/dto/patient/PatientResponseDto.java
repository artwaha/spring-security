package orci.or.tz.appointments.dto.patient;

import lombok.Data;
import orci.or.tz.appointments.enums.GenderEnum;

import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private Long id;
    private String registrationNumber;
    private String fullName;
    private LocalDate dob;
    private GenderEnum gender;
    private String mobile;
    private String patientType;
    private String billingCategory;
}