package orci.or.tz.appointments.services;

import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;


    public UserDetails LoadUserByRegno(String username) throws UsernameNotFoundException {

        User.UserBuilder builder = null;
        ApplicationUser user = patientRepo.findByRegistrationNumber(username).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        } else {
            builder = User.withUsername(username);
            builder.password("");
            builder.roles("");
        }
        return builder == null ? null : builder.build();
    }
    public ApplicationUser SavePatient(ApplicationUser p){
        return  patientRepo.save(p);
    }


    public List<ApplicationUser> GetAllPatients(Pageable pageable){
        return patientRepo.findAllByOrderByIdDesc(pageable);
    }

    public Long CountAllPatients(){
        return patientRepo.count();
    }

    public Optional<ApplicationUser> GetPatientById(Long id){
        return patientRepo.findById(id);
    }

    public Optional<ApplicationUser> GetPatientByRegistrationNumber(String registrationNumber) {
        return patientRepo.findByRegistrationNumber(registrationNumber);
    }


}
