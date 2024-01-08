package orci.or.tz.appointments.web.internal;

import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.web.internal.api.PatientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PatientsController implements PatientApi {

    @Autowired
    private PatientService patientService;

    @Autowired
    private Commons commons;

    @Override
    public ResponseEntity<GenericResponse<List<PatientResponseDto>>> ViewAllPatients(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<ApplicationUser> patients = patientService.GetAllPatients(pageRequest);
        List<PatientResponseDto> resp = new ArrayList<>();

        for(ApplicationUser p : patients){
            PatientResponseDto r = commons.GeneratePatient(p);
            resp.add(r);
        }

        GenericResponse<List<PatientResponseDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = patientService.CountAllPatients().intValue();
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);
        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<?> GetPatientById(Long id) throws ResourceNotFoundException {
        Optional<ApplicationUser> patient = patientService.GetPatientById(id);
        if (!patient.isPresent()) {
            throw new ResourceNotFoundException("Patient with provided ID [" + id + "] does not exist.");
        }

        PatientResponseDto resp = commons.GeneratePatient(patient.get());
        return ResponseEntity.ok(resp);
    }
}
