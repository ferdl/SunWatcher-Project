package com.ferry.sunservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mappt die URL /api/images-serve/** auf den physischen Ordner im Container
        registry.addResourceHandler("/api/images-serve/**")
                .addResourceLocations("file:/app/images/gallery/");
    }
}