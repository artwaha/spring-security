package orci.or.tz.appointments.utilities;

import orci.or.tz.appointments.dto.doctor.DocExternalDto;
import orci.or.tz.appointments.dto.doctor.DoctorInternalDto;
import orci.or.tz.appointments.dto.notification.SmsDto;
import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.models.Doctor;
import orci.or.tz.appointments.services.NotificationService;
import orci.or.tz.appointments.services.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;


@Component
public class Commons {
    @Autowired
    private Mapper mapper;

    @Autowired
    private RandomPasswordGenerator generator;

    @Autowired
    private PatientService patientService;

    @Autowired
    private NotificationService notificationService;


    public void GenerateOTP(ApplicationUser u) {

        String password = generator.generateRandomNumbers(6);
        u.setOtp(password);
        u.setValidUntil(LocalDateTime.now().toLocalDate().atTime(23,59,59));

        patientService.SavePatient(u);
        String msg = "Ndugu " + u.getFullName()
                + ", Tafadhali tumia nenosiri hili "
                + u.getOtp() + " kuingia katika mfumo.";
        SmsDto sms = new SmsDto();
        sms.setSms(msg);
        sms.setMobile(u.getMobile());
        notificationService.SendSMSToQueue(sms);

    }

    public PatientResponseDto GeneratePatient(ApplicationUser p) {
        ModelMapper modelMapper = mapper.getModelMapper();
        PatientResponseDto d = modelMapper.map(p, PatientResponseDto.class);
        return d;
    }


    public PatientDto GeneratePatientDTO(ApplicationUser p) {
        ModelMapper modelMapper = mapper.getModelMapper();
        PatientDto patient = modelMapper.map(p, PatientDto.class);
        return patient;
    }

    // Generate the DoctorExternalDto
    public DocExternalDto GenerateDoctorExternalDto(Doctor doctor) {
        ModelMapper modelMapper = mapper.getModelMapper();
        DocExternalDto doctorExternalDto = modelMapper.map(doctor, DocExternalDto.class);
        return doctorExternalDto;
    }

    public DocExternalDto GenerateDoctorDto(Doctor doctor) {
        ModelMapper modelMapper = mapper.getModelMapper();
        DocExternalDto doctorExternalDto = modelMapper.map(doctor, DocExternalDto.class);
        return doctorExternalDto;
    }

    public DoctorInternalDto GenerateDoctorInternalDto (JsonNode jsonNode) {
        ModelMapper modelMapper = mapper.getModelMapper();
        DoctorInternalDto doctorInternalDto = modelMapper.map(jsonNode, DoctorInternalDto.class);
        return doctorInternalDto;
    }

    //This functions returns the total number of pages
    public Integer GetTotalNumberOfPages(Integer totalCount, Integer pageSize) {
        Integer results;

        if (totalCount % pageSize == 0) {
            results = totalCount / pageSize;
        } else {
            results = totalCount / pageSize + 1;
        }

        return results;
    }


}
