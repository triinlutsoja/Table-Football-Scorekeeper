package com.football.Table_Football_Scorekeeper_API;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;


@Configuration
public class LoggingFilterConfig {
    @Bean
    public Filter loggingFilter() {
        return (request, response, chain) -> {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            System.out.println("Request: " + req.getMethod() + " " + req.getRequestURI());
            System.out.println("Headers: " + Collections.list(req.getHeaderNames()));
            chain.doFilter(request, response);
            System.out.println("Response Status: " + res.getStatus());
            System.out.println("Response Headers: " + res.getHeaderNames());
        };
    }
}
