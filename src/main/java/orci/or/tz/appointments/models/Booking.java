

package orci.or.tz.appointments.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import orci.or.tz.appointments.enums.BookingStatusEnum;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Table(name = "booking")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "booking_id_seq")
    @SequenceGenerator(name = "booking_id_seq", sequenceName = "BOOKING_ID_SEQ", initialValue = 999999990, allocationSize = 1)
    @Column(name = "booking_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private ApplicationUser patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate = LocalDate.now();

    @Column(name = "is_pushed", nullable = false)
    private boolean isPushed = false;

    @Column(name = "booking_status", nullable = false)
    private BookingStatusEnum bookingStatus = BookingStatusEnum.PENDING;

}
