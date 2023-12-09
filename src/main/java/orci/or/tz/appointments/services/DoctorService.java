package orci.or.tz.appointments.services;

import orci.or.tz.appointments.models.Doctor;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
<<<<<<< HEAD
import org.springframework.stereotype.Service;

import orci.or.tz.appointments.repository.DoctorRepository;
=======

import orci.or.tz.appointments.repository.DoctorRepository;
import  orci.or.tz.appointments.models.Doctor;
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
@Service
=======
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
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
<<<<<<< HEAD
        return doctorRepository.findByInayaId(id);
    }

    public int countTotalItems() {
        return (int) doctorRepository.count();
    }

    public boolean CheckIfDoctorExistsByInayaId(Integer inayaId) {
        return doctorRepository.existsByInayaId(inayaId);
=======
        return doctorRepository.findByInayaId(Integer id);

>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
    }
}