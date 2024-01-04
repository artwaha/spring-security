

package orci.or.tz.appointments.repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.enums.BookingStatusEnum;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByIdAndPatient(Long id, ApplicationUser patient);
    List<Booking> findAllByPatientOrderByCreatedDateDesc(ApplicationUser patient, Pageable pageable);
    List<Booking> findAllByOrderByCreatedDateDesc( Pageable pageable);

    List<Booking> findAllByBookingStatusAndPatientOrderByCreatedDateDesc(
        BookingStatusEnum bookingStatus, ApplicationUser patient, Pageable pageable
    );


    // A custom to search appointments by Date Range and Status
    List<Booking> findAllByCreatedDateBetweenAndBookingStatusAndPatientOrderByCreatedDateDesc(
        LocalDate startDate, LocalDate endDate, BookingStatusEnum bookingStatus, ApplicationUser patient, Pageable pageable
    );


    List<Booking> findAllByCreatedDateBetweenAndPatientOrderByCreatedDateDesc(
        LocalDate startDate, LocalDate endDate, ApplicationUser patient, Pageable pageable
    );

    // this method will return the appointments tat are upcoming and not Attended or cancelled
    List<Booking> findByPatientAndBookingStatusIn(ApplicationUser patient, 
        List<BookingStatusEnum> bookingStatusList, Pageable pageable);


    // Custom method to find all upcoming Appointments by patient, booking status, and within a date range
    List<Booking> findByPatientAndBookingStatusInAndAppointmentDateBetween( ApplicationUser patient, 
        List<BookingStatusEnum> bookingStatusList, LocalDate startDate, LocalDate endDate, Pageable pageable);


    // A method to count the items according to theri status
    long countByBookingStatusAndPatient(BookingStatusEnum bookingStatus, ApplicationUser patient);

    long countByBookingStatusAndCreatedDateBetweenAndPatient(BookingStatusEnum bookingStatus, 
        LocalDate startDate, LocalDate endDate, ApplicationUser patient);

    long countByCreatedDateBetweenAndPatient(LocalDate statDate, LocalDate enDate, ApplicationUser patient);

    long countByPatient(ApplicationUser patient);

    // Count All the Upcoming Appointments That A patient Has Not Attended
    long countByPatientAndBookingStatusIn(ApplicationUser patient, List<BookingStatusEnum> bookingStatusList);

     // Custom method to count the number of items for the specified conditions for the appointmnents based on Date Range
     long countByPatientAndBookingStatusInAndAppointmentDateBetween(ApplicationUser patient, 
        List<BookingStatusEnum> bookingStatusList, LocalDate startDate, LocalDate endDate);

     // By Status

    List<Booking> findByBookingStatusOrderByCreatedDateDesc(BookingStatusEnum bookingStatus, Pageable pageable);
    long countByBookingStatus(BookingStatusEnum bookingStatus);
    List<Booking> findByBookingStatusAndAppointmentDateBetweenOrderByCreatedDateDesc(BookingStatusEnum bookingStatus,LocalDate statDate, LocalDate enDate, Pageable pageable);
    long countByBookingStatusAndAppointmentDateBetween(BookingStatusEnum bookingStatus,LocalDate statDate, LocalDate enDate);
}
