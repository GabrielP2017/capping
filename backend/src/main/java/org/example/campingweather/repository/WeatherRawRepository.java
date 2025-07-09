package org.example.campingweather.repository;

import org.example.campingweather.domain.WeatherRaw;
import org.example.campingweather.dto.WeatherLatestDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRawRepository extends CrudRepository<WeatherRaw, Long> {

    @Query("""
        SELECT new org.example.campingweather.dto.WeatherLatestDto(
            w.nx,
            w.ny,
            w.category,
            w.fcstDate,
            w.fcstTime,
            w.fcstValue
        )
        FROM WeatherRaw w
        WHERE w.nx = :nx AND w.ny = :ny
        ORDER BY w.baseDate DESC, w.baseTime DESC
    """)
    WeatherLatestDto findTopByNxAndNyOrderByBaseDateDescBaseTimeDesc(
            Double nx, Double ny);
}
