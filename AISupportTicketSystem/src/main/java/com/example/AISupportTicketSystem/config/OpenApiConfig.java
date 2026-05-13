package com.example.AISupportTicketSystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI aiSupportTicketOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Support Ticket API")
                        .version("1.0")
                        .description("REST API for AI-powered support ticket management"));
    }
}
