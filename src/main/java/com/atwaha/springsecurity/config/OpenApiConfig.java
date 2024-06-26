package com.atwaha.springsecurity.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring Security Demo Project",
                description = "REST API Definition/Specification",
                contact = @Contact(
                        name = "Abdul-razak Twaha",
                        email = "Abdul-razak.Twaha@outlook.com"
                ),
                version = "1.0"
        )
)
@SecurityScheme(
        name = "jba",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP
)
/* Define Security Requirement in the controller/method you want to secure */
public class OpenApiConfig {
}
