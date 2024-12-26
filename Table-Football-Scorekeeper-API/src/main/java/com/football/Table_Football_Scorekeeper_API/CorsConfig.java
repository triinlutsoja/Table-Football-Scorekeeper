package com.football.Table_Football_Scorekeeper_API;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// If you want the frontend to communicate with the backend, make sure:
//	•	Your backend is running on localhost:8080.
//	•	Your browser allows CORS (Cross-Origin Resource Sharing) or your backend has configured CORS to accept frontend requests.

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allows CORS for all paths
                .allowedOrigins("http://localhost:5500") // Frontend URL (adjust according to your frontend port)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Allowed HTTP methods
                .allowedHeaders("*") // Allow any headers
                .allowCredentials(true); // Allow cookies, if needed
    }
}
