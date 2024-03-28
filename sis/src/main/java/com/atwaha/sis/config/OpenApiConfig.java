package com.atwaha.sis.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
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
        }
//        security = {
//                /* Define here if you want to implement this for all end-points */
//                /* For controller lever requirements, add this line below to individual controllers or METHOD level */
//                @SecurityRequirement(name = "JWT")
//        }
)
@SecurityScheme(
        name = "JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP
)
public class OpenApiConfig {
}
