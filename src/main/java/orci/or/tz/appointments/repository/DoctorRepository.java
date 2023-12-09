package orci.or.tz.appointments.repository;

import orci.or.tz.appointments.models.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByInayaId(Integer id);

    List<Doctor> findAllByOrderByIdDesc(Pageable pageable);

    boolean existsByInayaId(Integer inayaId);
}
