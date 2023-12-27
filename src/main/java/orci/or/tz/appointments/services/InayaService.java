package orci.or.tz.appointments.services;
import orci.or.tz.appointments.dto.booking.*;

import com.squareup.okhttp.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class InayaService {

    @Value("${orci.inaya.api.url}")
    private String inayaUrl;

    @Value("${orci.inaya.api.specialists.url}")
    private String inayaSpecialistsUrl;

    @Value("${orci.inaya.api.specialistById.url}")
    private String inayaSpecialistByIdUrl;

    @Value("${orci.inaya.api.appointment.count.to.a.specialist}")
    private String inayaSpecialistBookingsCount;

    public String GetPatient(String regNo) throws IOException {
        String inayaUrlRegNOEndpoint = inayaUrl+regNo;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(inayaUrlRegNOEndpoint)
                .build();
        Response response = client.newCall(request).execute();

        try{
            if (response.isSuccessful() && response.code() == 200){
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }

    }

    public String GetAllSpecialists() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(inayaSpecialistsUrl)
                .build();
        Response response = client.newCall(request).execute();

        try{
            if (response.isSuccessful() && response.code() == 200){
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    public String GetSpecialistFromInayaById(Integer inayaId) throws IOException {

        String getSpecialistFromInayaByIdEndpoint = inayaSpecialistByIdUrl+inayaId;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getSpecialistFromInayaByIdEndpoint)
                .build();
        Response response = client.newCall(request).execute();

        try{
            if (response.isSuccessful() && response.code() == 200){
                return response.body().string();
            } else {
                return null;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }


    public String GetAppointmentsCountToASpecificSpecialist(Integer id, LocalDate appointmentDate) {
        try {
            // Prepare the request body
            BookingCountRequestDto bookingCountRequestDto = new BookingCountRequestDto();
            bookingCountRequestDto.setBookingDate(appointmentDate);
            bookingCountRequestDto.setDoctorId(id);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Set request entity
            HttpEntity<BookingCountRequestDto> requestEntity = new HttpEntity<>(bookingCountRequestDto, headers);

            // Make the POST request
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.postForObject(inayaSpecialistBookingsCount, requestEntity, String.class);

            // Process the response
            if (response != null) {
                System.out.println("COUNTS FOR APPOINTMENT ->" + response);
                return response;
            } else {
                System.out.println("Response is null");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            // Handle the exception or rethrow as needed
            return null;
        }
    }


    

}