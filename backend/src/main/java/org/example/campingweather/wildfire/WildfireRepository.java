package org.example.campingweather.wildfire;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

public interface WildfireRepository extends ReactiveCrudRepository<WildfireEntity, Long> {

    /** 위‧경도로 1 시간 이내 최신 1건 */
@Query("""
    SELECT *
    FROM   wildfire_risks
    WHERE  lat = :lat
      AND  lon = :lon
      AND  fetched_at >= NOW() - INTERVAL '1 hour'
    ORDER BY fetched_at DESC
    LIMIT 1
""")
Mono<WildfireEntity> findLatest(double lat, double lon);


}
