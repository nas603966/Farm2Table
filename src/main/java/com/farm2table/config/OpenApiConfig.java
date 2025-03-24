package com.farm2table.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Farm2Table API Documentation",
                version = "1.0",
                description = "API for managing Farm2Table Marketplace"
        )
)
public class OpenApiConfig {

}