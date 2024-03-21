package com.atwaha.sis.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

//        modelMapper.typeMap(StudentRequest.class, Student.class).addMappings(mapper -> {
//            mapper.map(source -> studentRepository.findById(source.getSchoolId()).orElseThrow(), Student::setSchool);
//        });

        return modelMapper;
    }
}
