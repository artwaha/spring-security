package orci.or.tz.appointments.utilities;

import orci.or.tz.appointments.dto.notification.SmsDto;
import orci.or.tz.appointments.dto.patient.PatientDto;
import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.models.ApplicationUser;
import orci.or.tz.appointments.services.NotificationService;
import orci.or.tz.appointments.services.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;


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


}
