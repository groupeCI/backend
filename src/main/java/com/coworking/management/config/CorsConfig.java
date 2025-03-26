package com.coworking.management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class CorsConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // ou vos autres origines frontales
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // Configuration pour servir les fichiers uploadés
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("file:" + uploadDir + "/")
                        .setCachePeriod(3600)
                        .resourceChain(true)
                        .addResolver(new PathResourceResolver());
                
                // Configuration optionnelle pour les favicon et autres ressources
                registry.addResourceHandler("/**")
                        .addResourceLocations("classpath:/static/");
            }
        };
    }
}