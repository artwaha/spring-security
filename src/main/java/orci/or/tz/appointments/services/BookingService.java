package orci.or.tz.appointments.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.repository.BookingRepository;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    public Booking SaveAppointment(Booking b){
        return  bookingRepository.save(b);
    }


    public List<Booking> GetAllPatientAppointments(ApplicationUser patient, Pageable pageable){
        return bookingRepository.findAllByPatientOrderByCreatedDateDesc(patient, pageable);
    }

    public Optional<Booking> GetAppointmentById(Long id){
        return bookingRepository.findById(id);
    }

    public Optional<Booking> GetAppointmentByIdForASpecificPatient(Long id, ApplicationUser patient) {
        return bookingRepository.findByIdAndPatient(id, patient);
    }

    public List<Booking> GetAllPatientAppointmentsByBookingStatus(
        BookingStatusEnum bookingStatus, ApplicationUser patient, Pageable pageable) {
        return bookingRepository.findAllByBookingStatusAndPatientOrderByCreatedDateDesc(bookingStatus, patient, pageable);
    }

    public List<Booking> GetAllPatientAppointmentsByDateRangeAndBookingStatus(LocalDate startDate,
     LocalDate endDate, BookingStatusEnum bookingStatus, ApplicationUser patient, Pageable pageable) {
        return bookingRepository.findAllByCreatedDateBetweenAndBookingStatusAndPatientOrderByCreatedDateDesc(startDate, endDate, bookingStatus, patient, pageable);
    }

    public List<Booking> GetAllAppointmnentsByDateRange(LocalDate startDate,
     LocalDate endDate, ApplicationUser patient, Pageable pageable) {
        return bookingRepository.findAllByCreatedDateBetweenAndPatientOrderByCreatedDateDesc(startDate, endDate, patient, pageable);
    }

    //Get All the Upcomming Appointments that are not attended or cancelled
    public List<Booking> GetAllUpcomingAppointments(ApplicationUser patient, List<BookingStatusEnum> bookingStatusList, Pageable pageable) {
        return bookingRepository.findByPatientAndBookingStatusIn(patient, bookingStatusList, pageable );
    }

    // Get All the Upcoming Appointments Based On Date Range
    public List<Booking> GetAllUpCommingAppointmentsBasedOnDateRange( ApplicationUser patient, 
    List<BookingStatusEnum> bookingStatusList, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return bookingRepository.findByPatientAndBookingStatusInAndAppointmentDateBetween(patient,
        bookingStatusList,startDate, endDate, pageable);
    }




    // THE METHODS BELOW ARE JUST FOR THE COUNTS

    public int countTotalItems() {
        return (int) bookingRepository.count();
    }

    public int countAppointmentsByBookingStatusAndDateRange(BookingStatusEnum bookingStatus, LocalDate startDate, LocalDate enDate, ApplicationUser patient) {
        return (int) bookingRepository.countByBookingStatusAndCreatedDateBetweenAndPatient(bookingStatus, startDate, enDate, patient);
    }

    // this counts the total items according to the status if the appointmnent e.g "pending"
    public int countAppointmentsByBookingStatusAndPatient(BookingStatusEnum bookingStatus, ApplicationUser patient) {
        return (int) bookingRepository.countByBookingStatusAndPatient(bookingStatus, patient);
    }

    public int countAppointmnetsByDateRangeAndPatient(LocalDate statDate, LocalDate enDate, ApplicationUser patient){
        return (int) bookingRepository.countByCreatedDateBetweenAndPatient(statDate, enDate, patient);
    }

    public int countAppointmentsForASpecificPatient(ApplicationUser patient) {
        return (int) bookingRepository.countByPatient(patient);
    }

    //Count the total of All Upcomo=ing Appointments
    public int countAllUpcomingAppointmetsForASpecificPatient(ApplicationUser patient, List<BookingStatusEnum> bookingStatusList) {
        return (int) bookingRepository.countByPatientAndBookingStatusIn(patient, bookingStatusList);
    }

    //count All Upcoming Appoints Based On date Range for A specific Patient
    public int countAllUpcomingAppointmetsForASpecificPatientBasedOndateRange(ApplicationUser patient, 
    List<BookingStatusEnum> bookingStatusList, LocalDate startDate, LocalDate endDate) {
        return (int) bookingRepository.countByPatientAndBookingStatusInAndAppointmentDateBetween(patient, bookingStatusList,
        startDate, endDate);
    }


    public List<Booking> GetAllAppointments( Pageable pageable){
        return bookingRepository.findAllByOrderByCreatedDateDesc(pageable);
    }

    public int CountAllAppointments() {
        return (int) bookingRepository.count();
    }

    public List<Booking> GetAllAppointmentsByStatus( BookingStatusEnum status, Pageable pageable){
        return bookingRepository.findByBookingStatusOrderByCreatedDateDesc(status,pageable);
    }

    public int CountAllAppointmentsByStatus(BookingStatusEnum status) {
        return (int) bookingRepository.countByBookingStatus(status);
    }

    public List<Booking> GetAllAppointmentsByStatusAndDate( BookingStatusEnum status,LocalDate statDate, LocalDate enDate, Pageable pageable){
        return bookingRepository.findByBookingStatusAndAppointmentDateBetweenOrderByCreatedDateDesc(status,statDate,enDate,pageable);
    }

    public int CountAllAppointmentsByStatusAndDate(BookingStatusEnum status,LocalDate statDate, LocalDate enDate) {
        return (int) bookingRepository.countByBookingStatusAndAppointmentDateBetween(status,statDate,enDate);
    }

}
