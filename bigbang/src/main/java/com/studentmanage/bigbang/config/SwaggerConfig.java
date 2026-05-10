package com.studentmanage.bigbang.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bigBangOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生管理系统 — 宇宙爆炸版 API")
                        .description("BigBang Edition: Spring Boot 3.2 + JWT双Token + Redis + Flyway + MapStruct + AOP + 限流 + Docker")
                        .version("4.0.0")
                        .contact(new Contact().name("lechan775").url("https://github.com/lechan775/student_management"))
                        .license(new License().name("MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(new Components()
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
