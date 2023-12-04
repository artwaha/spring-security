package orci.or.tz.appointments.utilities;

import orci.or.tz.appointments.dto.patient.PatientResponseDto;
import orci.or.tz.appointments.models.ApplicationUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class Commons {
    @Autowired
    private Mapper mapper;


    public PatientResponseDto GeneratePatient(ApplicationUser p) {
        ModelMapper modelMapper = mapper.getModelMapper();
        PatientResponseDto d = modelMapper.map(p, PatientResponseDto.class);
        return d;
    }


}
