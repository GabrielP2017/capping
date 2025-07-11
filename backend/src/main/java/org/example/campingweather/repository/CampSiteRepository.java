package org.example.campingweather.repository;

import org.example.campingweather.domain.CampSite;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CampSiteRepository extends JpaRepository<CampSite, Long> {

    @Query("SELECT c.id FROM CampSite c")
    List<Long> findAllCampIds();

    /* 전체 목록은 JpaRepository 기본 findAll(Pageable) 사용 */

    /** 반경(m) 검색 + 페이징  */
    @Query(value = """
        SELECT * FROM camp_sites c
        WHERE ST_DWithin(
          geography(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)),
          geography(ST_SetSRID(ST_MakePoint(c.lon, c.lat), 4326)),
          :radius)
        """,
            countQuery = """
        SELECT count(*) FROM camp_sites c
        WHERE ST_DWithin(
          geography(ST_SetSRID(ST_MakePoint(:lon, :lat), 4326)),
          geography(ST_SetSRID(ST_MakePoint(c.lon, c.lat), 4326)),
          :radius)
        """,
            nativeQuery = true)
    Page<CampSite> findWithinRadius(@Param("lat") double lat,
                                    @Param("lon") double lon,
                                    @Param("radius") double radius,
                                    Pageable pageable);
}
