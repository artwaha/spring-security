package com.atwaha.sis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SchoolInformationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolInformationSystemApplication.class, args);
	}

}
