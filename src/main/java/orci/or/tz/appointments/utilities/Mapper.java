package orci.or.tz.appointments.utilities;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Mapper {

    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true).setAmbiguityIgnored(true);
        return modelMapper;
    }
}
