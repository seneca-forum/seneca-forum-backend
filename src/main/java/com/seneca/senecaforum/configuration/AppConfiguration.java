package com.seneca.senecaforum.configuration;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class AppConfiguration{

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("http://senecaforum.s3-website.us-east-2.amazonaws.com")
                        .allowedOriginPatterns("http://localhost:4200")
                        .exposedHeaders("Authorization")
                        .allowedMethods("GET","POST","PUT","DELETE");
                registry.addMapping("/ws/**")
                        .allowedOriginPatterns("http://senecaforum.s3-website.us-east-2.amazonaws.com")
                        .allowedOriginPatterns("http://localhost:4200")
                        .allowCredentials(true);
            }
        };
    }

    @Bean //ModelMapper config
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}
