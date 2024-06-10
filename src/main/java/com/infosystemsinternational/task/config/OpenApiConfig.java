package com.infosystemsinternational.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

/**
 * Configuration class for setting up OpenAPI for API documentation.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Creates and configures an OpenAPI bean.
     *
     * @return a customized OpenAPI object
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Infosystems International Task")
                        .version("1.0")
                        .description("API documentation for Infosystems International Task"));
    }
}
