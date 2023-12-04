package orci.or.tz.appointments.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orci.or.tz.appointments.utilities.Auditable;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Table(name="doctors")
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
}
