package com.devconnect.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI devConnectApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("DevConnect API")
                        .description("Developer Q&A Platform with AI Integration")
                        .version("1.0.0"));
    }
}
