package orci.or.tz.appointments.services;

import orci.or.tz.appointments.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import orci.or.tz.appointments.repository.DoctorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;


    public Doctor SaveDoctor(Doctor p){
        return  doctorRepository.save(p);
    }


    public List<Doctor> GetAllDoctors(Pageable pageable){
        return doctorRepository.findAllByOrderByIdDesc(pageable);
    }

    public Optional<Doctor> GetDoctorById(Long id){
        return doctorRepository.findById(id);
    }

    public Optional<Doctor> GetDoctorByInayaId(Integer id) {
        return doctorRepository.findByInayaId(id);
    }

    public int countTotalItems() {
        return (int) doctorRepository.count();
    }

    public boolean CheckIfDoctorExistsByInayaId(Integer inayaId) {
        return doctorRepository.existsByInayaId(inayaId);

    }
}