package orci.or.tz.appointments.consumers;

import orci.or.tz.appointments.dto.booking.BookingInayaDto;
import orci.or.tz.appointments.dto.booking.CancelDto;
import orci.or.tz.appointments.dto.notification.BookingDto;
import orci.or.tz.appointments.enums.BookingStatusEnum;
import orci.or.tz.appointments.models.Booking;
import orci.or.tz.appointments.services.BookingService;
import orci.or.tz.appointments.services.NotificationService;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

@Component
public class CancelationConsumer {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private NotificationService notificationService;

    @Value("${orci.inaya.api.cancel.url}")
    private String cancelationEndpoint;

    @RabbitListener(queues = "orci.cancelation.bookings")
    public void PushBookingToInaya(BookingDto b) {
        Optional<Booking> booking = bookingService.GetAppointmentById(b.getBookingId());
        System.out.println(" ----Booking hii  ->" + booking.get().getId());
        try {

            if (booking.isPresent()) {
                Booking bk = booking.get();
                CancelDto dto = new CancelDto();
                dto.setBookingId(bk.getId());
                dto.setCancelationReason(bk.getCancelationReason());

                // Send HTTP Request to Inaya API
                JSONObject json = new JSONObject(dto);
                System.out.println(" ----Data zinapelekwa Inaya  ->" + json);


                URL url = new URL(cancelationEndpoint);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                try(OutputStream os = con.getOutputStream()) {
                    byte[] input = json.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try(BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                        JSONObject json2 = new JSONObject(response.toString());

                        // Update Booking status to SUBMITTED if sent successful

//                        if (json2.getInt("code") == 200) {
//                            bk.setPushed(true);
//                            bk.setBookingStatus(BookingStatusEnum.UPCOMING);
//                            bookingService.SaveAppointment(bk);
//                        }
                    }
                    System.out.println(" ----Cancelation Response ->" +response.toString());
                }

            } else {
                System.out.println(" ----Booking haijapatikana  ");
            }

        } catch (Exception e) {
            System.out.println("Failed to Push Cancelation to Inaya -> " + e.getMessage());
            notificationService.ResendBookingToQueue(b);
        }
    }
}
