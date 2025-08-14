package org.example.campingweather.repository;

import org.example.campingweather.wildfire.WildfireEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WildfireRepository extends R2dbcRepository<WildfireEntity, Long> {

    Mono<WildfireEntity> findTopByRegionCodeOrderByFetchedAtDesc(String RegionCode);

    @Query("""
        INSERT INTO wildfire_risks(region_code, risk_level, fetched_at)
        VALUES (:#{#e.regionCode}, :#{#e.riskLevel}, :#{#e.fetchedAt})
        ON CONFLICT (region_code, fetched_at)
        DO UPDATE SET risk_level = EXCLUDED.risk_level
        RETURNING *
    """)
    Mono<WildfireEntity> upsert(@Param("e") WildfireEntity e);


}
