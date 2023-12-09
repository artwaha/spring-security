

package orci.or.tz.appointments.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orci.or.tz.appointments.utilities.Auditable;
import orci.or.tz.appointments.enums.DoctorStatusEnum;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Table(name = "doctors")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Doctor extends Auditable<String> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_id_seq")
    @SequenceGenerator(name = "doctor_id_seq", sequenceName = "DOCTOR_ID_SEQ", initialValue = 1, allocationSize = 1)
    @Column(name = "doctor_id")
    private Long id;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "inaya_id", nullable = false)
    private Integer inayaId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DoctorStatusEnum status = DoctorStatusEnum.ACTIVE;

    @Column(name = "monday", nullable = false)
    private boolean monday = false;

    @Column(name = "tuesday", nullable = false)
    private boolean tuesday = false;

    @Column(name = "wednesday", nullable = false)
    private boolean wednesday = false;

    @Column(name = "thursday", nullable = false)
    private boolean thursday = false;

    @Column(name = "friday", nullable=false)
    private boolean friday = false;
}
