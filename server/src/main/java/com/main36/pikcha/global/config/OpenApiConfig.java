package com.main36.pikcha.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Pikcha Backend Application API")
                .version("v1.0")
                .contact(new Contact().name("ys932184@gmail.com"))
                .description("Pikcha Backend Application API 명세서입니다.");

        return new OpenAPI().info(info);

    }

}
