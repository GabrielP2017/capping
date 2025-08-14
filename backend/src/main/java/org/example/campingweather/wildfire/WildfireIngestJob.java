package org.example.campingweather.wildfire;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.campingweather.repository.WildfireRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 2 시간마다(00, 02, 04 …) 17 개 도(시·도)별 산불위험도를
 * 공공데이터 API → WildfireEntity 로 변환 → DB(t_wildfire_risks) 에 적재한다.
 *
 */

@Slf4j
@Component
@EnableScheduling          // 스케줄러 활성화
@RequiredArgsConstructor    // client, repo 주입
public class WildfireIngestJob {

    /** 국립산림과학원 시·도 코드 17종 */
    private static final java.util.List<String> PROVINCE_CODES = java.util.List.of(
            "11","26","27","28","29","30","31","36",
            "41","42","43","44","45","46","47","48","50"
    );

    private final WildfireApiClient  client;
    private final WildfireRepository repo;

    /** 2 h 주기 배치 */
    @Scheduled(initialDelay=5_000, fixedDelay = 120*60*1_000, zone = "Asia/Seoul")
    public void ingest() {
        log.info("🔥 Province-level ingest start");
        repo.count().doOnNext(c -> log.info("🔥 wildfire rows={}", c)).subscribe();

        Flux.fromIterable(PROVINCE_CODES)
                // ① API 호출 → DTO
                .flatMap(client::fetchByProvince)
                // ② DTO → Entity
                .map(WildfireDto::toEntity)
                // ③ DB 저장
                .flatMap(repo::save)
                // ④ 결과 로그
                .collectList()
                .doOnNext(list -> log.info("✅ ingest done, rows={}", list.size()))
                .doOnError(e  -> log.error("❌ ingest error", e))
                .subscribe();
    }

}