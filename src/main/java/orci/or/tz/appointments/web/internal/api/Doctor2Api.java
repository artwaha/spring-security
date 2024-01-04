package orci.or.tz.appointments.web.internal.api;

import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.doctor.DocDto;
import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.dto.doctor.DoctorUpdateDto;
import orci.or.tz.appointments.dto.doctor.DoctorRequestDto;
import orci.or.tz.appointments.exceptions.OperationFailedException;
import orci.or.tz.appointments.exceptions.ResourceNotFoundException;
import orci.or.tz.appointments.utilities.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

@RequestMapping("/api/internal/doctors")
@Api(value = "Doctor Management", description = "Manage Doctors on the web")
public interface Doctor2Api {

    @ApiOperation(value = "View All Doctors From Inaya", notes = "View All Doctors From Inaya")
    @GetMapping(value = "/inaya/specialists/", produces = "application/json")
    ResponseEntity<List<JsonNode>> GetAllDoctorsFromInayaApi() 
    throws ResourceNotFoundException, OperationFailedException;
    

    @ApiOperation(value = "View All Doctors From Appointment DB", notes = "View All Doctors From Appointment DB")
    @GetMapping(value = "", produces = "application/json")
    ResponseEntity<GenericResponse<List<DocDto>>> GetAllDoctorsFromAppointmentDB(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) throws ResourceNotFoundException;

    @ApiOperation(value = "Create a doctor ", notes = "Create a doctor")
    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    ResponseEntity<DocDto > createDoctorIntoAppointmentDB(@Valid @RequestBody DoctorRequestDto doctorRequestDto)
            throws ResourceNotFoundException, IOException;
    
    @ApiOperation(value = "update a doctor ", notes = "Create a doctor")
    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    ResponseEntity<DocDto > UpdateDoctorIntoAppointmentDB(@Valid @RequestBody DoctorUpdateDto doctorUpdateDto,  @PathVariable Long id)
            throws ResourceNotFoundException;        
}
