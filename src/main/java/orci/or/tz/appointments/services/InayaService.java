package orci.or.tz.appointments.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;
import orci.or.tz.appointments.dto.patient.PatientDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InayaService {

    @Value("${orci.inaya.api.url}")
    private String inayaUrl;


    public PatientDto GetPatient(String regNo) throws IOException {

        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder().url(inayaUrl + regNo).method("GET", null).build();
        Response response = null; // Initialize the response outside the try block
        try {
            response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }
            System.out.println("Ma code =>" + response.code());


            if (response.code() == 200) {

                JSONObject json = new JSONObject(response.body().toString());
                System.out.println("Ma Dude =>" + String.valueOf(json));

                ObjectMapper objectMapper = new ObjectMapper();
                PatientDto resp = objectMapper.readValue(response.body().string(), PatientDto.class);

                return resp;
            }else{
                return null;
            }

        } catch (Exception e) {
            System.out.println("Ma Error =>" + e.getMessage());
            return null;
        }
    }
}
