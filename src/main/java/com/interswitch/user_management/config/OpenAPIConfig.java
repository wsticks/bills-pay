package com.interswitch.user_management.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @io.swagger.v3.oas.annotations.info.Info(
        title = "Bills Payment",
        description = "This is the API for Bills payment.",
        version = "v1.0"
))
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bills Payment Service Service")
                        .version("v1.0")
                        .contact(new io.swagger.v3.oas.models.info.Contact()
                                .name("Olumuywa Akanni")
                                .email("akannimuyiwa@gmail.com"))
                );
    }
}
