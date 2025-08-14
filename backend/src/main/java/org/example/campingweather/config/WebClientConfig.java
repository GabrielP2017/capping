package org.example.campingweather.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${wildfire.api.base-url}")
    private String baseUrl;

    @Bean
    public WebClient wildfireWebClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

}
