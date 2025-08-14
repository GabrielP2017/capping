package org.example.campingweather.wildfire;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
@Slf4j
public class WildfireApiClient {

    private final WebClient webClient;

    @Value("${wildfire.api.service-key}")
    private String serviceKey;

    @Value("${wildfire.api.base-url}")       // ★ base-url 주입
    private String apiBase;

    @PostConstruct
    void logKeys() {
        log.info("⚠️ API_BASE={}", apiBase);
        log.info("⚠️ SERVICE_KEY length={}", serviceKey.length());
    }

    public Mono<WildfireDto> fetchByProvince(String regionCode) {
        return webClient.get()
                .uri(b -> b
                        .path("/forestPointListSidoSearch")
                        .queryParam("serviceKey", serviceKey, UriUtils.encode(serviceKey, StandardCharsets.UTF_8))
                        .queryParam("numOfRows", 1)
                        .queryParam("pageNo", 1)
                        .queryParam("localAreas", regionCode)
                        .queryParam("excludeForecast", 0)
                        .queryParam("_type", "json")
                        .build())
                .retrieve()
                .bodyToMono(WildfireResponse.class)
                .flatMap(resp -> {
                    // 안전하게 아이템 리스트 꺼내기
                    var items = java.util.Optional.ofNullable(resp.getResponse())
                            .map(WildfireResponse.ResponseWrapper::getBody)
                            .map(WildfireResponse.Body::getItems)
                            .map(WildfireResponse.ItemWrapper::getItem)
                            .orElse(java.util.List.of());

                    // 데이터가 없으면 Mono.empty() → 이후 스트림에서 자동 skip
                    if (items.isEmpty()) return Mono.empty();

                    // 최신 1건만 DTO로 변환
                    return Mono.just(WildfireDto.from(items.get(0)));
                });

    }

}
