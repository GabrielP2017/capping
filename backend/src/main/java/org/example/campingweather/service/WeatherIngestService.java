package org.example.campingweather.service;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.domain.WeatherRaw;
import org.example.campingweather.repository.WeatherRawRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherIngestService {

    private final WebClient kmaWebClient;           // 2단계에서 Bean 등록
    private final WeatherRawRepository weatherRepo;

    @Value("${KMA_API_KEY}")      // .env에 넣어 둔 서비스키
    private String serviceKey;

    /** 단일 격자(nx,ny)에 대해 최신 예보·실황 데이터 수집 → 저장 */
    // public Mono<Void> ingestGrid(double nx, double ny) {

    //     // ① KMA 요청 파라미터 조립 ─────────────
    //     String baseDate = DateTimeUtil.nowKst().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    //     String baseTime = DateTimeUtil.alignToHour(DateTimeUtil.nowKst(), 1) // 바로 직전 1시간
    //                                .format(DateTimeFormatter.ofPattern("HHmm"));

    //     return kmaWebClient
    //             .get()
    //             .uri(uriBuilder -> uriBuilder
    //                     .path("/getVilageFcst")
    //                     .queryParam("serviceKey", serviceKey)
    //                     .queryParam("pageNo", 1)
    //                     .queryParam("numOfRows", 1000)
    //                     .queryParam("dataType", "JSON")
    //                     .queryParam("base_date", baseDate)
    //                     .queryParam("base_time", baseTime)
    //                     .queryParam("nx", (int) nx)
    //                     .queryParam("ny", (int) ny)
    //                     .build())
    //             .retrieve()
    //             .bodyToMono(KmaResponse.class)      // 2. 응답 DTO(아래)로 역직렬화
    //             .doOnNext(resp ->                      // 3. Entity → saveAll
    //                     weatherRepo.saveAll(resp.toEntities(nx, ny)))
    //             .then();                              // 4. 리액티브 체인 완료
    // }
}
