package orci.or.tz.appointments.services;


import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class LoggedUser {

    @Autowired
    private PatientRepository userRepo;

    public ApplicationUser getInfo() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)  {

            ApplicationUser user = userRepo.findByRegistrationNumber(auth.getName()).orElse(null);
            if (user != null){

                return user;
            }
            return null;
        }
        return null;

    }
}
