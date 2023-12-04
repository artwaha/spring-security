package orci.or.tz.appointments.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orci.or.tz.appointments.enums.GenderEnum;
import orci.or.tz.appointments.utilities.Auditable;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Table(name="patients")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationUser extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_id_seq")
    @SequenceGenerator(name = "patient_id_seq", sequenceName = "PATIENT_ID_SEQ", initialValue = 1, allocationSize = 1)
    @Column(name = "patient_id")
    private Long id;

    @Column(name = "registration_number", nullable = false)
    private String registrationNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "dob", nullable = false)
    private LocalDate dob;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Column(name = "mobile", nullable = false)
    private String mobile;


    @Column(name = "billing_category", nullable = false)
    private String billingCategory;

    @Column(name = "otp", nullable = true)
    private String otp;

    @Column(name = "valid_until", nullable = true)
    private LocalDateTime validUntil;


    public ApplicationUser(Long id) {
        super();
        this.id = id;
    }

    public ApplicationUser(String registrationNumber, String otp) {
    }

}
