package org.example.campingweather.dto.kma;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.campingweather.domain.WeatherRaw;

import java.util.List;
import java.util.stream.Collectors;

public record KmaResponse(
    @JsonProperty("response") ResponseBody response
) {
    public record ResponseBody(@JsonProperty("body") Body body) {}
    public record Body(@JsonProperty("items") Items items) {}
    public record Items(@JsonProperty("item") List<Item> item) {}

    public record Item(
        String baseDate,
        String baseTime,
        int nx,
        int ny,
        String category,
        String fcstDate,
        String fcstTime,
        String fcstValue
    ){}

    /**  DTO → 엔티티 매핑 */
    public List<WeatherRaw> toEntities(double nx, double ny) {
        return response.body.items.item.stream()
                .map(it -> WeatherRaw.builder()
                        .baseDate(it.baseDate)
                        .baseTime(it.baseTime)
                        .nx(nx)
                        .ny(ny)
                        .category(it.category)
                        .fcstDate(it.fcstDate)
                        .fcstTime(it.fcstTime)
                        .fcstValue(it.fcstValue)
                        .build())
                .collect(Collectors.toList());
    }
}
