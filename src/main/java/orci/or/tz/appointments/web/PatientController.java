package orci.or.tz.appointments.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.dto.patient.PatientUpdateMobileDto;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.enums.GenderEnum;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.services.PatientService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.web.api.PatientApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.pattern.PathPattern;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.util.Optional;

@RestController
public class PatientController implements PatientApi {

    @Autowired
    private InayaService inayaService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private Commons commons;


    @Override
    public ResponseEntity<PatientDto> GetPatientByRegistration(String regNo) throws ResourceNotFoundException, IOException, OperationFailedException {
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

                    PatientDto patient = commons.GeneratePatientDTO(patientToBeSavedInAppointmentDB);
                    return ResponseEntity.ok(patient);

                } else if (codeValue == 404) {
                    throw new ResourceNotFoundException("Patient Not Found");
                } else {
                    throw new OperationFailedException("The operation failed due to a specific condition");
                }
            }
        }else{

        }

        PatientDto patient = commons.GeneratePatientDTO(patientFromDB.get());
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<PatientDto> UpdatePatientMobileNumber(@RequestBody PatientUpdateMobileDto patientUpdateMobileDto) throws ResourceNotFoundException, IOException {
        String regNo = patientUpdateMobileDto.getRegistrationNumber();
        String mobile = patientUpdateMobileDto.getMobile();
        Optional<ApplicationUser> user = patientService.GetPatientByRegistrationNumber(regNo);

        try {
            if (!user.isPresent()) {
                throw  new ResourceNotFoundException("Patient Not Found");
            } else {
                ApplicationUser patient = user.get();
                patient.setMobile(mobile);
                patient.setConfirmed(true);
                patientService.SavePatient(patient);
                commons.GenerateOTP(patient);

                PatientDto customizedPatient = commons.GeneratePatientDTO(patient);
                return ResponseEntity.ok(customizedPatient);
            }
        } catch (Exception e) {
            e.getMessage();
            throw new IOException("There is internal operation errors");
        }


    }

}