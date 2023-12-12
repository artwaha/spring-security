package orci.or.tz.appointments.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.repository.BookingRepository;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;

    public Booking SaveAppointment(Booking b){
        return  bookingRepository.save(b);
    }


    public List<Booking> GetAllAppointments(Pageable pageable){
        return bookingRepository.findAllByOrderByIdDesc(pageable);
    }

    public Optional<Booking> GetAppointmentById(Long id){
        return bookingRepository.findById(id);
    }

    public int countTotalItems() {
        return (int) bookingRepository.count();
    }
}
