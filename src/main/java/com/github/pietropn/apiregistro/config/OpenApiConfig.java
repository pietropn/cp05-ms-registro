package com.github.pietropn.apiregistro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiRegistroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Registro Meteorológico")
                        .description("API REST para registro e gerenciamento de medições meteorológicas - CP2 2026/02")
                        .version("v1")
                        .contact(new Contact()
                                .name("Pietro - RM 557000")
                                .url("https://github.com/pietropn")));
    }
}
