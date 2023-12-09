package orci.or.tz.appointments.web.external;

import orci.or.tz.appointments.dto.doctor.DoctorExternalDto;
import orci.or.tz.appointments.services.InayaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class DoctorExternalController {

    @Autowired
    private InayaService inayaService;

    @Override
    public ResponseEntity<DoctorExternalDto> GetAllDoctors() {
        String doctorsResponseFromInaya = inayaService.GetAllSpecialists();

        if (doctorsResponseFromInaya == null) {
            // I have to return that the doctors are present from inaya
        }
    }

}
