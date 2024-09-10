package com.ridango.game;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean 
    ObjectMapper ObjectMapper(){
        return new ObjectMapper();
    }

    @Bean WebClient webClient() {
        return WebClient.create();
    }

}
