package orci.or.tz.appointments.web.external;



import orci.or.tz.appointments.dto.doctor.DoctorExternalDto;
import orci.or.tz.appointments.dto.patient.PatientUpdateMobileDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import orci.or.tz.appointments.dto.patient.PatientDto;n;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@RequestMapping("api/external/doctor")
@Api(value = "Doctor Management", description = "Manage Doctors on the web")
public interface DoctorExternalApi {

    @ApiOperation(value = "View All Doctors", notes = "View All Doctors")
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    ResponseEntity<DoctorExternalDto> GetAllDoctors();


}


