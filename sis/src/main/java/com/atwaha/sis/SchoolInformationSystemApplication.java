package com.atwaha.sis;

import com.atwaha.sis.model.dto.RegisterRequest;
import com.atwaha.sis.repository.UserRepository;
import com.atwaha.sis.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static com.atwaha.sis.model.enums.Role.ADMIN;
import static com.atwaha.sis.model.enums.Role.USER;

@SpringBootApplication
@RequiredArgsConstructor
public class SchoolInformationSystemApplication implements CommandLineRunner {
    private final AuthService authService;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(SchoolInformationSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean adminExists = userRepository.existsByEmailIgnoreCase("admin@domain.com");
        if (adminExists) return;
        RegisterRequest admin = RegisterRequest.builder().firstName("Admin").lastName("Admin").role(ADMIN).email("admin@domain.com").password("123").build();
        authService.register(admin);

        boolean userExists = userRepository.existsByEmailIgnoreCase("sholmes@domain.com");
        if (userExists) return;
        RegisterRequest user = RegisterRequest.builder().firstName("Sherlock").lastName("Holmes").role(USER).email("sholmes@domain.com").password("123").build();
        authService.register(user);

    }
}
