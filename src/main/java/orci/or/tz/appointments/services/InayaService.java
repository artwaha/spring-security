package orci.or.tz.appointments.services;
import orci.or.tz.appointments.dto.booking.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

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
            System.out.println(e.getMessage());
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

    public String GetAppointmentsCountToASpecificSpecialist(Integer id, LocalDate appointmentDate) 
    throws IOException {

        BookingCountRequestDto bookingCountRequestDto = new BookingCountRequestDto();
        bookingCountRequestDto.setBookingDate(appointmentDate);
        bookingCountRequestDto.setDoctorId(id);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequestBody = objectMapper.writeValueAsString(bookingCountRequestDto);

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonRequestBody);
        Request request = new Request.Builder()
                            .url(inayaSpecialistBookingsCount)
                            .post(body)
                            .addHeader("content-Type", "application/json")
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

}
