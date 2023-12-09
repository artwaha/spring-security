package orci.or.tz.appointments.repository;

<<<<<<< HEAD
=======
import orci.or.tz.appointments.models.ApplicationUser;
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
import orci.or.tz.appointments.models.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

<<<<<<< HEAD
@Repository
=======
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByInayaId(Integer id);

    List<Doctor> findAllByOrderByIdDesc(Pageable pageable);
<<<<<<< HEAD

    boolean existsByInayaId(Integer inayaId);
=======
>>>>>>> f1f853977caf628cb150e6da779d92705c40eac3
}
