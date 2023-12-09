
package orci.or.tz.appointments.web.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.web.external.api.DoctorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import orci.or.tz.appointments.services.DoctorService;
import orci.or.tz.appointments.services.InayaService;
import orci.or.tz.appointments.utilities.Commons;
import orci.or.tz.appointments.utilities.GenericResponse;
import orci.or.tz.appointments.exceptions.*;
import orci.or.tz.appointments.models.Doctor;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DoctorExternalController implements DoctorApi {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private InayaService inayaService;

    @Autowired
    private Commons commons;

    @Override
    public ResponseEntity<GenericResponse<List<DocExternalDto>>> GetAllDoctors(int page, int size) throws ResourceNotFoundException {
        List<DocExternalDto> resp = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page, size);

        try {
            String doctorsFromInaya = inayaService.GetAllSpecialists();
            if (doctorsFromInaya == null) {
                throw new ResourceNotFoundException("At Current No Specialists Fetched From Inaya");
            } else {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(doctorsFromInaya);

                int codeValue = jsonNode.hasNonNull("code") ? jsonNode.get("code").asInt() : 0;
                if (codeValue == 200) {
                    JsonNode dataArray = jsonNode.get("data");
                    if (dataArray.isArray()) {
                        for (JsonNode doctorDataNodeFromInaya : dataArray) {
                            String fullName = doctorDataNodeFromInaya.get("fullName").asText();
                            int inayaId = doctorDataNodeFromInaya.get("id").asInt();
                            if (!doctorService.CheckIfDoctorExistsByInayaId(inayaId)) {
                                Doctor doctor = new Doctor();
                                doctor.setDoctorName(fullName);
                                doctor.setInayaId(inayaId);
                                doctorService.SaveDoctor(doctor);
                                DocExternalDto doctorExternalDto = commons.GenerateDoctorExternalDto(doctor);
                                resp.add(doctorExternalDto);
                            } else {
                                Optional<Doctor> doctor = doctorService.GetDoctorByInayaId(inayaId);
                                if (doctor.isPresent()) {
                                    throw new ResourceNotFoundException("The Doctor with the provided Id does not exist in AppointmentDB");
                                }
                                DocExternalDto doctorExternalDto = commons.GenerateDoctorExternalDto(doctor.get());
                                resp.add(doctorExternalDto);
                            }
                        }
                        GenericResponse<List<DocExternalDto>> response = new GenericResponse<>();
                        response.setCurrentPage(page);
                        response.setPageSize(size);
                        Integer totalCount = doctorService.countTotalItems();
                        response.setTotalItems(totalCount);
                        response.setTotalPages(commons.GetTotalNumberOfPages(totalCount, size));
                        response.setData(resp);
                        return ResponseEntity.ok(response);
                    } else {
                        System.out.println("\"data\" is not an array.");
                    }
                } else {
                    throw new ResourceNotFoundException("No Specialists Found From Inaya");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

            List<Doctor> doctors = doctorService.GetAllDoctors(pageRequest);
            for (Doctor doctor : doctors) {
                DocExternalDto doctorExternalDto = commons.GenerateDoctorExternalDto(doctor);
                resp.add(doctorExternalDto);
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

        return null;
    }
}
