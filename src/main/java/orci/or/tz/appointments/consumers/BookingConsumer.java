package orci.or.tz.appointments.consumers;

import com.squareup.okhttp.*;
import orci.or.tz.appointments.dto.booking.BookingInayaDto;
import orci.or.tz.appointments.dto.notification.BookingDto;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.services.BookingService;
import orci.or.tz.appointments.services.NotificationService;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Component
public class BookingConsumer {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private NotificationService notificationService;

    @Value("${orci.inaya.api.booking.url}")
    private String bookingEndpoint;

    @RabbitListener(queues = "orci.appointment.bookings")
    public void PushBookingToInaya(BookingDto b){
        Optional<Booking> booking = bookingService.GetAppointmentById(b.getBookingId());
        System.out.println( " ----Booking hii  ->" + booking.get().getId() );
        try{

            if(booking.isPresent()){
                Booking bk = booking.get();
                BookingInayaDto dto = new BookingInayaDto();
                dto.setId(bk.getId());
                dto.setAppointmentDate(bk.getAppointmentDate());
                dto.setRegNo(bk.getPatient().getRegistrationNumber());
                dto.setDoctorId(Long.valueOf(bk.getDoctor().getInayaId()));

                // Send HTTP Request to Inaya API

                JSONObject json = new JSONObject(dto);
                System.out.println( " ----Data zinapelekwa Inaya  ->" + String.valueOf(json));

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(bookingEndpoint)
                        .post(RequestBody.create(
                                MediaType.parse("application/json"), String.valueOf(json)))
                        .build();

                Response response = client.newCall(request).execute();

//                HttpRequest req = HttpRequest.newBuilder()
//                        .uri(URI.create(bookingEndpoint))
//                        .header("Content-Type", "application/json")
//                        .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(json)))
//                        .build();
//
//                var client = HttpClient.newHttpClient();
//                var httpresponse = client.send(httprequest, HttpResponse.BodyHandlers.ofString());

                if (response.isSuccessful() && response.code() == 200) {

                    JSONObject json2 = new JSONObject(response.body());
                    System.out.println(" ----Inaya Response ->" + String.valueOf(json2));

                    // Update Booking status to SUBMITTED if sent successful

                    if (json2.getInt("code") == 200) {
                        bk.setPushed(true);
                        bk.setBookingStatus(BookingStatusEnum.UPCOMING);
                        bookingService.SaveAppointment(bk);
                    }
                }
            }else{
                System.out.println( " ----Booking haijapatikana  " );
            }

        }catch (Exception e){
            System.out.println("Failed to Push Booking to Inaya -> " + e.getMessage());
            notificationService.ResendBookingToQueue(b);
        }
    }
}
