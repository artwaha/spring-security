package orci.or.tz.appointments.web.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.enums.GenderEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.web.internal.api.PatientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class PatientsController implements PatientApi {

    @Autowired
    private PatientService patientService;

    @Autowired
    private InayaService inayaService;

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

    @Override
    public ResponseEntity<PatientResponseDto> GetPatientByRegistration(String regNo) throws ResourceNotFoundException, IOException, OperationFailedException {
        Optional<ApplicationUser> patientFromDB = patientService.GetPatientByRegistrationNumber(regNo);

        if (!patientFromDB.isPresent()) {
            String patientFromInaya = inayaService.GetPatient(regNo);

            if (patientFromInaya == null) {
                throw new ResourceNotFoundException("Patient Not Found");
            } else {
                // Parse JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(patientFromInaya);

                // Access the "code" field
                int codeValue = jsonNode.hasNonNull("code") ? jsonNode.get("code").asInt() : 0;
                if (codeValue == 200) {
                    ApplicationUser patientToBeSavedInAppointmentDB = new ApplicationUser();

                    // set the regNo
                    patientToBeSavedInAppointmentDB.setRegistrationNumber(regNo);

                    // Handle null values gracefully
                    patientToBeSavedInAppointmentDB.setFullName(jsonNode.path("data").path("fullName").asText(null));

                    String dobString = jsonNode.path("data").path("dob").asText(null);


                    if (dobString != null) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate dob = LocalDate.parse(dobString, formatter);
                        patientToBeSavedInAppointmentDB.setDob(dob);
                    }

                    String genderString = jsonNode.path("data").path("gender").asText(null);
                    GenderEnum gender = (genderString != null) ? GenderEnum.valueOf(genderString.toUpperCase()) : null;
                    patientToBeSavedInAppointmentDB.setGender(gender);
                    patientToBeSavedInAppointmentDB.setConfirmed(false);
                    patientToBeSavedInAppointmentDB.setMobile(jsonNode.path("data").path("mobile").asText(null));
                    patientToBeSavedInAppointmentDB.setRegistrationNumber(regNo);
                    patientToBeSavedInAppointmentDB.setBillingCategory(jsonNode.path("data").path("billingCategory").asText(null));

                    // set the status of the patient
                    String status = jsonNode.path("data").path("status").asText(null);
                    patientToBeSavedInAppointmentDB.setStatus(status);

                    // Now save the patient in the database
                    patientService.SavePatient(patientToBeSavedInAppointmentDB);

                    PatientResponseDto patient = commons.GeneratePatient(patientToBeSavedInAppointmentDB);
                    return ResponseEntity.ok(patient);

                } else if (codeValue == 404) {
                    throw new ResourceNotFoundException("Patient Not Found");
                } else {
                    throw new OperationFailedException("The operation failed due to a specific condition");
                }
            }

        }else{

            ApplicationUser user = patientFromDB.get();
            PatientResponseDto patientDto = commons.GeneratePatient(user);
            return ResponseEntity.ok(patientDto);

        }
    }
}
