package org.example.campingweather.wildfire;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WildfireApiClient {

    private final WebClient webClient;

    public WildfireApiClient(@Value("${WILDFIRE_API_URL}") String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Mono<WildfireDto> fetchRisk(String regionCode) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("regId", regionCode).build())
                .retrieve()
                .bodyToMono(WildfireDto.class);
    }


}
