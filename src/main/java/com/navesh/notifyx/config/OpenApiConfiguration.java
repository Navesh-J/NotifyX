package com.navesh.notifyx.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI notifyXOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("NotifyX API")
                                .version("1.0.0")
                                .description("""
                                        Production-ready Notification Service
                                        
                                        Features:
                                        • Email Notifications
                                        • SMS Notifications
                                        • Push Notifications
                                        • Composite Notifications
                                        • Audit Logging
                                        • Spring AOP
                                        • Strategy + Factory Pattern
                                        """)
                                .contact(
                                        new Contact()
                                                .name("Navesh")
                                                .email("navesh.professional@gmail.com")
                                )
                                .license(
                                        new License()
                                                .name("MIT")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("NotifyX Documentation")
                )
                .components(
                        new Components()
                                .addResponses(
                                        "BadRequest",
                                        new ApiResponse().description("Invalid Request")
                                )
                                .addResponses(
                                        "InternalError",
                                        new ApiResponse().description("Internal Server Error")
                                )
                );
    }
}
