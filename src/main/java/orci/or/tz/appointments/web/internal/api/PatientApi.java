package orci.or.tz.appointments.web.internal.api;

import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/api/internal/patients")
public interface PatientApi {

    @ApiOperation(value = "View All Patients", notes = "View All Patients")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<PatientResponseDto>>> ViewAllPatients(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    );

    @ApiOperation(value = "Get Patient By Id", notes = "Get Patient By Id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> GetPatientById(@PathVariable Long id) throws ResourceNotFoundException;

    @ApiOperation(value = "View All Patient By Registration Number", notes = "View All Patient By Registration Number")
    @RequestMapping(value = "regno/", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<PatientDto> GetPatientByRegistration(@RequestParam String regNo) throws ResourceNotFoundException, IOException, OperationFailedException;
}
