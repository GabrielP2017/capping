package org.example.campingweather.repository;

import org.example.campingweather.domain.WeatherRaw;
import org.springframework.data.repository.CrudRepository;

public interface WeatherRawRepository extends CrudRepository<WeatherRaw, Long> {
    @org.springframework.data.jpa.repository.Query("""
        SELECT new map(w.category as cat, w.fcstValue as val)
        FROM WeatherRaw w
        WHERE w.campId = :campId
          AND w.category IN ('TMP','POP')
        ORDER BY w.baseDate DESC, w.baseTime DESC
    """)
    java.util.List<java.util.Map<String,String>>
        findLatest(@org.springframework.data.repository.query.Param("campId") long campId,
                   org.springframework.data.domain.Pageable page);
}
