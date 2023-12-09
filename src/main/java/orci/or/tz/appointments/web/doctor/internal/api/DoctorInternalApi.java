package orci.or.tz.appointments.web.doctor.internal.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import orci.or.tz.appointments.dto.doctor.external.DoctorExternalDto;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;

import java.util.List;

@RequestMapping("/api/internal/doctors/")
@Api(value = "Doctor Management", description = "Manage Doctors on the web")
public interface DoctorInternalApi {

    @ApiOperation(value = "View All Doctors", notes = "View All Doctors")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<DoctorExternalDto>>> GetAllDoctors(
        @RequestParam(defaultValue = "0", required = false) int page,
        @RequestParam(defaultValue = "10", required = false) int size
    ) throws ResourceNotFoundException;

    
}





