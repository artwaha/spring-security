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

}
