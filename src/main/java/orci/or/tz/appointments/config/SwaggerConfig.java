package orci.or.tz.appointments.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;


@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket externalApi() {
        ApiInfoBuilder builder = new ApiInfoBuilder().title("ORCI patients External Appointments API")
                .description("Documentation automatically generated").version("1.0.0")
                .contact(new Contact("ORCI Developers", "orci.or.tz", "abdulhemedi99@gmail.com"));
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("external") // Unique name for the Docket
                .select()
                .apis(RequestHandlerSelectors.basePackage("orci.or.tz.appointments.web.external"))
                .paths(PathSelectors.any()).build().securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext())).apiInfo(builder.build());
    }

    // This is for external doctors API

    // This is for internal doctors API
    @Bean
    public Docket internalApi() {
        ApiInfoBuilder builder = new ApiInfoBuilder().title("ORCI Doctors Internal API")
                .description("Documentation automatically generated").version("1.0.0")
                .contact(new Contact("ORCI Developers", "orci.or.tz", "abdulhemedi99@gmail.com"));
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("internal") // Unique name for the Docket
                .select()
                .apis(RequestHandlerSelectors.basePackage("orci.or.tz.appointments.web.internal"))
                .paths(PathSelectors.any()).build().securitySchemes(Arrays.asList(securityScheme()))
                .securityContexts(Arrays.asList(securityContext())).apiInfo(builder.build());
    }





    private SecurityScheme securityScheme() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("JWT", scopes())))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes() {
        AuthorizationScope[] scopes = {};
        return scopes;
    }


}


