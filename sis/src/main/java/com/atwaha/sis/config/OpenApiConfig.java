package com.atwaha.sis.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "School Project",
                description = "REST API Definition/Specification",
                contact = @Contact(
                        name = "Abdul-razak Twaha",
                        email = "Abdul-razak.Twaha@outlook.com",
                        url = "https://www.orci.or.tz"
                ),
                version = "1.0"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Host - Development Server"
                ),
                @Server(
                        url = "http://192.168.1.8",
                        description = "Production Server"
                )
        },
        security = {
                @SecurityRequirement(name = "JWT - Bearer Authentication")
        }
)
@SecurityScheme(
        name = "JWT - Bearer Authentication",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP
)
public class OpenApiConfig {
}
