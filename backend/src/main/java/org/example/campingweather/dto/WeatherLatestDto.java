package org.example.campingweather.dto;

public record WeatherLatestDto(
        Double nx,         // grid x
        Double ny,         // grid y
        String     category,   // 예: TMP, POP …
        String     fcstDate,   // YYYYMMDD
        String     fcstTime,   // HHmm
        String     fcstValue   // 예보값
) {}
