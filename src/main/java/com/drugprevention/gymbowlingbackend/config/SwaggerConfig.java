package com.drugprevention.gymbowlingbackend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gym Bowling Backend API - TEAM TESTING")
                        .version("1.0")
                        .description("API cho ứng dụng Gym Bowling - TẠM THỜI DISABLE SECURITY ĐỂ TEST")
                        .contact(new Contact()
                                .name("Gym Bowling Team")   
                                .email("support@gymbowling.com")))
                .servers(List.of(
                    new Server().url("https://ae332185633a.ngrok-free.app").description("Ngrok HTTPS Server - CURRENT"),
                    new Server().url("http://localhost:8080").description("Local Development Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", 
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập Firebase ID Token")));
    }
}
