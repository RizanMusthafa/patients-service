package io.github.rizanmusthafa.patient_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static files under /dashboard/**
        // This includes index.html, JS, CSS, images, and other assets
        registry.addResourceHandler("/dashboard/**")
                .addResourceLocations("classpath:/static/dashboard/");
    }
}
