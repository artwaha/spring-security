package orci.or.tz.appointments.web.external.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api/external/doctors/")
@Api(value = "Doctor Management", description = "Manage Doctors on the web")
public interface DoctorApi {
    @ApiOperation(value = "View All Doctors", notes = "View All Doctors")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<DocExternalDto>>> GetAllDoctors(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) throws ResourceNotFoundException;
}
