package com.highlight.weather.legacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    /**
     * OpenAPI bean 구성
     */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("날씨기능 Controller")
                .version("1.0")
                .description("날씨기능을 위해 구현된 RestController들을 테스트합니다.");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
