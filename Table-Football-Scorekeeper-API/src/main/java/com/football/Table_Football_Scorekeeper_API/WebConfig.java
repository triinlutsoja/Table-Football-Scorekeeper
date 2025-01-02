package com.football.Table_Football_Scorekeeper_API;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("*") // Allow requests from everywhere
                        .allowedMethods("GET", "POST", "PUT", "DELETE"); // Allow these HTTP methods
                        // .allowCredentials(true); // Allow cookies, if needed
            }
        };
    }
}