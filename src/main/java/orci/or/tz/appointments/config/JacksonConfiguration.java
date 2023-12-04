package orci.or.tz.appointments.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        // Up to Jackson 2.9: (but not with 3.0)
        // @formatter:off
        ObjectMapper mapper = new ObjectMapper();

        // Hibernate 5 Module
        Hibernate5Module hibernateModule = new Hibernate5Module()
                .disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);

        // Modules
        mapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule()) // new module, NOT JSR310Module
                .registerModule(hibernateModule);

        // Features
        mapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(JsonParser.Feature.IGNORE_UNDEFINED)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

        // Inclusions
       // mapper
               // .setSerializationInclusion(Include.NON_NULL);
        // @formatter:on

        return mapper;
    }

    @Bean
    public XmlMapper xmlMapper() {
        // Up to Jackson 2.9: (but not with 3.0)
        // @formatter:off
        XmlMapper mapper = new XmlMapper();

        // Hibernate 5 Module
        Hibernate5Module hibernateModule = new Hibernate5Module()
                .disable(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION);

        // Modules
        mapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule()) // new module, NOT JSR310Module
                .registerModule(hibernateModule);

        // Features
        mapper
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .enable(JsonParser.Feature.IGNORE_UNDEFINED)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonGenerator.Feature.IGNORE_UNKNOWN);

        // Inclusions
        mapper
                .setSerializationInclusion(Include.NON_NULL);
        // @formatter:on

        return mapper;
    }
}

