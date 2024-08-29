package com.example.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.RestTemplate;

@Configuration
//@EnableWebSecurity
public class AppConfig {

    @Bean
    public RestTemplate template(){
        return new RestTemplate();
    }

}