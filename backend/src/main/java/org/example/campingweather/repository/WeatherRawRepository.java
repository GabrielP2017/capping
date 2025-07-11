package org.example.campingweather.repository;

import org.example.campingweather.domain.WeatherRaw;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface WeatherRawRepository extends JpaRepository<WeatherRaw, Long> {

    @Query("""
        SELECT new map(w.category as cat, w.fcstValue as val)
        FROM WeatherRaw w
        WHERE w.campId = :campId
          AND w.category IN ('TMP','POP')
        ORDER BY w.baseDate DESC, w.baseTime DESC
    """)
    List<Map<String, String>> findLatest(
        @Param("campId") long campId,
        Pageable page
    );
}
