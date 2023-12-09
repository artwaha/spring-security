package orci.or.tz.appointments.services;

import orci.or.tz.appointments.models.Doctor;
import org.json.JSONStringer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import orci.or.tz.appointments.repository.DoctorRepository;
import  orci.or.tz.appointments.models.Doctor;

import java.util.List;
import java.util.Optional;

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
        return doctorRepository.findByInayaId(Integer id);

    }
}