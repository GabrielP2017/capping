package org.example.campingweather.wildfire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

@Component                    // 스프링이 Bean 으로 등록
@RequiredArgsConstructor       // final 필드 3개를 받는 생성자 자동 생성
public class WildfireIngestJob {

    private final WildfireApiClient client;  // 3) 산림청 API 호출기
    private final WildfireRepository repo;   // 4) DB 저장용 Spring Data R2DBC/JPA
    private final RegionMapper regionMapper; // 5) 행정코드 ↔ 위·경도 변환 유틸

    /** 매시 10분마다 실행 (예: 01:10, 02:10, …) */
    @Scheduled(cron = "0 10 * * * *")
    public void ingest() {
        // 6) 시군구 코드 전체 순회
        // 1) 시‧군‧구 코드 전체 순회 (동기 forEach)
        regionMapper.allRegionCodes().forEach(code -> {
            // 2) 해당 코드의 위도‧경도
            LatLon coord = regionMapper.toLatLon(code);

            // 3) WebClient → DTO → Entity → DB 저장
            client.fetchRisk(code)                       // Mono<WildfireDto>
                    .map(dto -> dto.toEntity(coord))       // Mono<WildfireEntity>
                    .flatMap(repo::save)                  // DB insert
                    .subscribe();                         // 전체 체인을 실행
        });

    }

}
