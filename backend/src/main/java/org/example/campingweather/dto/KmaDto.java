package org.example.campingweather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.campingweather.domain.WeatherRaw;

import java.util.List;
import java.util.stream.Collectors;

/**
 * KMA 공공데이터 Open API의 JSON 응답을 매핑하는 DTO.
 */
public record KmaDto(@JsonProperty("response") Response response) {

    public record Response(
        @JsonProperty("body") Body body
    ) {}

    public record Body(
        @JsonProperty("items") Items items
    ) {}

    public record Items(
        @JsonProperty("item") List<Item> item
    ) {}

    public record Item(
        @JsonProperty("baseDate")  String baseDate,
        @JsonProperty("baseTime")  String baseTime,
        @JsonProperty("fcstDate")  String fcstDate,
        @JsonProperty("fcstTime")  String fcstTime,
        @JsonProperty("category")  String category,   // TMP, POP 등
        @JsonProperty("fcstValue") String fcstValue,
        @JsonProperty("nx")        int nx,
        @JsonProperty("ny")        int ny
    ) {}

    /**
     * 이 DTO를 WeatherRaw 엔티티 리스트로 변환한다.
     */
    public List<WeatherRaw> toWeatherRawEntities(long campId) {
        return response
            .body()
            .items()
            .item()
            .stream()
            .map(it -> WeatherRaw.builder()
                .campId(campId)
                .baseDate(it.baseDate())
                .baseTime(it.baseTime())
                .category(it.category())
                .fcstDate(it.fcstDate())
                .fcstTime(it.fcstTime())
                .fcstValue(it.fcstValue())
                .nx((double) it.nx())
                .ny((double) it.ny())
                .build()
            )
            .collect(Collectors.toList());
    }
}
