
package orci.or.tz.appointments.web.external;

import java.util.ArrayList;
import java.util.List;

import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.web.external.api.DoctorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import orci.or.tz.appointments.services.DoctorService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.models.Doctor;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DoctorExternalController implements DoctorApi {

    @Autowired
    private DoctorService doctorService;

  
    @Autowired
    private Commons commons;

    @Override
    public ResponseEntity<GenericResponse<List<DocExternalDto >>> GetAllDoctorsFromAppointmentDBExternal(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);

    List<Doctor> doctors = doctorService.GetAllDoctors(pageRequest);

        List<DocExternalDto > resp = new ArrayList<>();
        for (Doctor doctor : doctors) {
            DocExternalDto  dr = commons.GenerateDoctorExternalDto(doctor);
            resp.add(dr);
        }

        
        GenericResponse<List<DocExternalDto>> response = new GenericResponse<>();
        response.setCurrentPage(page);
        response.setPageSize(size);
        Integer totalCount = doctorService.countTotalItems();
        response.setTotalItems(totalCount);
        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
        response.setData(resp);

        return ResponseEntity.ok(response);

    }

}
