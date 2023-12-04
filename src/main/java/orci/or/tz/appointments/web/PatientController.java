package orci.or.tz.appointments.web;

import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.web.api.PatientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.pattern.PathPattern;

import java.io.IOException;

@RestController
public class PatientController implements PatientApi {

   @Autowired
   private InayaService inayaService;

    @Override
    public ResponseEntity<PatientDto> GetPatientByRegistration(String regNo) throws ResourceNotFoundException, IOException {

        PatientDto p = inayaService.GetPatient(regNo);

        if(p == null){
            throw new ResourceNotFoundException("Patient Not Found");

        }else{
            return ResponseEntity.ok(p);
        }
    }
}
