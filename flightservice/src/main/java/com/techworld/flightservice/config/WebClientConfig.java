package com.techworld.flightservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${flight-search-service.baseurl}")
    private String flightSearchServiceBaseurl;

    @Bean
    public WebClient webClient() {

        return WebClient.builder()
                .baseUrl(flightSearchServiceBaseurl)
                .build();
    }
}
