package com.artwaha.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@PropertySource("classpath:mustard.properties")
@Service
public class ExampleService {

//    Inject Environment Bean
    private final Environment environment;

    @Value("${my.love}")
    private String myLove;

    public ExampleService(Environment environment) {
        this.environment = environment;
    }

    public String getOSname() {
        return getEnvironment().getProperty("os.name");
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getMyLove() {
        return myLove;
    }
}
