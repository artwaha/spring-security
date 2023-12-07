package orci.or.tz.appointments.tasks;

import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TokenValidityTask {

    @Autowired
    private PatientService userService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void setTokensInvalid(){
        Pageable page = null;
        List<ApplicationUser> users =  userService.GetAllPatients(page);
        for(ApplicationUser u : users){
            u.setOtp(null);
            u.setValidUntil(null);
            userService.SavePatient(u);
        }
    }
}
