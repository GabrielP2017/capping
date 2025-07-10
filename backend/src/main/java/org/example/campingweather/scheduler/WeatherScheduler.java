package org.example.campingweather.scheduler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.campingweather.dto.KmaDto;
import org.example.campingweather.mapper.KmaMapper;
import org.example.campingweather.repository.CampSiteRepository;
import org.example.campingweather.repository.WeatherRawRepository;
import org.example.campingweather.util.LatLonConverter;
import org.example.campingweather.util.LatLonConverter.NxNy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeatherScheduler {

    private final WebClient            kmaWebClient;
    private final CampSiteRepository   campRepo;
    private final WeatherRawRepository weatherRepo;
    private final LatLonConverter      converter;
    private final KmaMapper            mapper;

    /** 30분 주기 KMA 수집 */
    @Scheduled(cron = "0 */30 * * * *")
    public void fetch() {

        String baseDate = LocalDate.now()
                                   .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = latestTime();   // 0000·0300 … 최근 값

        campRepo.findAll().forEach(site -> {

            // CampSite.lat / lon 이 BigDecimal 이라면 double 로 변환
            NxNy xy = converter.toGrid(site.getLat().doubleValue(),
                                       site.getLon().doubleValue());

            Mono<KmaDto> res = kmaWebClient.get()
                .uri(b -> b.queryParam("serviceKey", System.getenv("KMA_API_KEY"))
                           .queryParam("dataType",  "JSON")
                           .queryParam("numOfRows", "1000")
                           .queryParam("base_date", baseDate)
                           .queryParam("base_time", baseTime)
                           .queryParam("nx", xy.nx())
                           .queryParam("ny", xy.ny())
                           .build())
                .retrieve()
                .bodyToMono(KmaDto.class);

            res.map(dto -> mapper.toEntities(site.getId(), dto))
               .doOnNext(weatherRepo::saveAll)
               .subscribe();
        });
    }

    private String latestTime() {
        int h = (java.time.LocalTime.now().getHour() / 3) * 3;
        return String.format("%02d00", h);
    }
}
