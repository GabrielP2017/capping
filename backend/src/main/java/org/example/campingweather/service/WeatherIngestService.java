package org.example.campingweather.service;

import org.example.campingweather.dto.KmaDto;
import org.example.campingweather.domain.WeatherRaw;
import org.example.campingweather.repository.CampSiteRepository;
import org.example.campingweather.repository.WeatherRawRepository;
import org.example.campingweather.util.GridConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WeatherIngestService {

    private final WebClient kmaWebClient;
    private final CampSiteRepository campSiteRepository;
    private final WeatherRawRepository weatherRawRepository;
    private final String apiKey;

    public WeatherIngestService(
        WebClient kmaWebClient,
        CampSiteRepository campSiteRepository,
        WeatherRawRepository weatherRawRepository,
        @Value("${kma.service.key}") String apiKey
    ) {
        this.kmaWebClient = kmaWebClient;
        this.campSiteRepository = campSiteRepository;
        this.weatherRawRepository = weatherRawRepository;
        this.apiKey = apiKey;
    }

    public void ingestOne(long campId) {
        List<WeatherRaw> raws = fetchForOneCamp(campId);
        if (!raws.isEmpty()) {
            weatherRawRepository.saveAll(raws);
        }
    }

    private List<WeatherRaw> fetchForOneCamp(long campId) {
        String baseDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = computeLatestBaseTime();

        var grid = GridConverter.toGrid(getLatitude(campId), getLongitude(campId));

        KmaDto dto = kmaWebClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/getVilageFcst")
                .queryParam("serviceKey", apiKey)
                .queryParam("dataType", "JSON")
                .queryParam("numOfRows", 1000)
                .queryParam("pageNo", 1)
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", grid.nx())
                .queryParam("ny", grid.ny())
                .build()
            )
            .retrieve()
            .bodyToMono(KmaDto.class)
            .block();

        return dto.toWeatherRawEntities(campId);
    }

    private String computeLatestBaseTime() {
        int h = LocalDateTime.now().getHour();
        return String.format("%02d00", (h / 3) * 3);
    }

    // TODO: 실제 DB에서 캠핑장 위·경도 조회 로직으로 대체
    private double getLatitude(long campId)  { return 37.5665; }
    private double getLongitude(long campId) { return 126.9780; }
}
