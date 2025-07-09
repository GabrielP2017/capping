package org.example.campingweather.wildfire;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WildfireApiClient {

    private final WebClient webClient;

    public WildfireApiClient(
            @Value("${WILDFIRE_API_URL}") String baseUrl,
            WebClient.Builder builder            // <-- 주입
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    /** regionCode(행정코드) → 위험 DTO */
    public Mono<WildfireDto> fetchRisk(String regionCode) {
        // ★ 일단 목 데이터를 돌려 코드 컴파일부터 되게!
        WildfireDto dummy = new WildfireDto();
        dummy.setRegionCode(regionCode);
        dummy.setRiskLevel(3);
        return Mono.just(dummy);
    }


}
