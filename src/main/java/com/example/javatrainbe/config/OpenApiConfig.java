package com.example.javatrainbe.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Cấu hình OpenAPI/Swagger
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Student Management API")
                        .description("API quản lý sinh viên - Java Training Project\n\n"
                                + "## APIs:\n"
                                + "- **Authentication**: Login, Register\n"
                                + "- **Student CRUD**: Get List, Get by ID, Create, Update, Delete\n"
                                + "- **Batch**: Export student data to CSV\n\n"
                                + "## Tech Stack:\n"
                                + "- Spring Boot 3.4 + Spring Security (JWT)\n"
                                + "- Doma2 (Database Access)\n"
                                + "- Lombok\n"
                                + "- MySQL + Docker\n"
                                + "- Spring Batch (CSV Export)")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Student Management Team")
                                .email("admin@student.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Bearer Authentication")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Nhập JWT token (không cần prefix 'Bearer')")));
    }
}
