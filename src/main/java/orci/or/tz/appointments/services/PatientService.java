package orci.or.tz.appointments.services;

import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepo;


    public ApplicationUser SavePatient(ApplicationUser p){
        return  patientRepo.save(p);
    }


    public List<ApplicationUser> GetAllPatients(Pageable pageable){
        return patientRepo.findAllByOrderByIdDesc(pageable);
    }

    public Optional<ApplicationUser> GetPatientById(Long id){
        return patientRepo.findById(id);
    }


}
