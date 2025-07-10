package org.example.campingweather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record KmaDto(@JsonProperty("response") Response response) {
    public record Response(@JsonProperty("body") Body body) {}
    public record Body(@JsonProperty("items") Items items) {}
    public record Items(@JsonProperty("item") List<Item> item) {}
    public record Item(
            @JsonProperty("baseDate") String baseDate,
            @JsonProperty("baseTime") String baseTime,
            @JsonProperty("fcstDate") String fcstDate,
            @JsonProperty("fcstTime") String fcstTime,
            @JsonProperty("category") String category,   // TMP, POP …
            @JsonProperty("fcstValue") String fcstValue,
            @JsonProperty("nx") int nx,
            @JsonProperty("ny") int ny
    ) {}
}
