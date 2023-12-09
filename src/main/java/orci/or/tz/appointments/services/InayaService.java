package orci.or.tz.appointments.services;

import com.squareup.okhttp.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class InayaService {

    @Value("${orci.inaya.api.url}")
    private String inayaUrl;

    @Value("${orci.inaya.api.specialists.url}")
    private String inayaSpecialistsUrl;

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
<<<<<<< HEAD
            return null;
=======
            throw e;
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
        }


    }

}
