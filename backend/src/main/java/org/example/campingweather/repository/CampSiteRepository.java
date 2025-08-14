package org.example.campingweather.repository;

import org.example.campingweather.domain.CampSite;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CampSiteRepository
        extends R2dbcRepository<CampSite, Long> {

    /*─────────────────────────────────────────────────────────*
     *  ❶ 좌표-기반 UPSERT
     *     - lat + lon 에 UNIQUE(또는 PK) 인덱스가 있어야
     *       ON CONFLICT 가 동작합니다.
     *─────────────────────────────────────────────────────────*/
    @Modifying
    @Query("""
        INSERT INTO camp_sites (name, address, lat, lon,
                                phone, region_code, last_updated)
        VALUES (:#{#c.name}, :#{#c.address}, :#{#c.lat}, :#{#c.lon},
                :#{#c.phone}, :#{#c.regionCode}, now())
        ON CONFLICT (lat, lon)           -- 👈  유니크 키
        DO UPDATE
           SET name         = EXCLUDED.name,
               address      = EXCLUDED.address,
               phone        = EXCLUDED.phone,
               region_code  = EXCLUDED.region_code,
               last_updated = now()
        """)
    Mono<Void> upsert(@Param("c") CampSite c);

    /** 반경(m) + 페이징 결과 */
    @Query("""
        SELECT * FROM camp_sites c
        WHERE ST_DWithin(
          geography(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)),
          geography(ST_SetSRID(ST_MakePoint(c.lon, c.lat), 4326)),
          :radius)
        /* Pageable 바인딩 ↓ */
        LIMIT :#{#pageable.pageSize}
        OFFSET :#{#pageable.offset}
        """)
    Flux<CampSite> findWithinRadius(double lat,
                                    double lon,
                                    double radius,
                                    Pageable pageable);

    /** 전체 개수 – Page 객체 만들 때 사용 */
    @Query("""
        SELECT count(*) FROM camp_sites c
        WHERE ST_DWithin(
          geography(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)),
          geography(ST_SetSRID(ST_MakePoint(c.lon, c.lat), 4326)),
          :radius)
        """)
    Mono<Long> countWithinRadius(double lat,
                                 double lon,
                                 double radius);

    /** LIMIT / OFFSET 페이징 */
    @Query("""
        SELECT * FROM camp_sites
        LIMIT :limit OFFSET :offset
        """)
    Flux<CampSite> findPage(long limit, long offset);

    /** 총 행 개수 */
    @Query("SELECT count(*) FROM camp_sites")
    Mono<Long> countAll();

    @Query("SELECT DISTINCT region_code FROM camp_sites")
    Flux<String> findDistinctRegionCodes();

    Mono<CampSite> findFirstByRegionCode(String regionCode);

}