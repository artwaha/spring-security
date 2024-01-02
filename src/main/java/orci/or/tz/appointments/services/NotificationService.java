package orci.or.tz.appointments.services;


import orci.or.tz.appointments.dto.notification.BookingDto;
import orci.or.tz.appointments.dto.notification.EmailDto;
import orci.or.tz.appointments.dto.notification.SmsDto;
import orci.or.tz.appointments.models.Booking;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void SendEmailActivationToQueue(EmailDto dto) {
        rabbitTemplate.convertAndSend("orci.notifications", "emails@3592", dto);
    }

    public void SendSMSToQueue(SmsDto dto) {
        rabbitTemplate.convertAndSend("orci.notifications", "sms@3592", dto);
    }

    public void SendBookingToQueue(Booking dto) {
        BookingDto b = new BookingDto();
        b.setBookingId(dto.getId());
        rabbitTemplate.convertAndSend("orci.bookings", "bookings@3592", b);
    }

}
