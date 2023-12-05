package orci.or.tz.appointments.repository;

import orci.or.tz.appointments.models.ApplicationUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<ApplicationUser,Long> {

    List<ApplicationUser> findAllByOrderByIdDesc(Pageable pageable);
    Optional<ApplicationUser> findByRegistrationNumber(String registrationNumber);
}
