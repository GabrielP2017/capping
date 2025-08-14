package org.example.campingweather.seed;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.campingweather.domain.CampSite;
import org.example.campingweather.repository.CampSiteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Component
@Profile("local")
@RequiredArgsConstructor
public class CampCsvSeeder implements CommandLineRunner {

    private final CampSiteRepository repo;

    // 도명(한글) → 도코드 매핑
    static final Map<String,String> PROVINCE_MAP = Map.ofEntries(
            Map.entry("서울특별시","11"),
            Map.entry("부산광역시","26"),
            Map.entry("대구광역시","27"),
            Map.entry("인천광역시","28"),
            Map.entry("광주광역시","29"),
            Map.entry("대전광역시","30"),
            Map.entry("울산광역시","31"),
            Map.entry("세종특별자치시","36"),
            Map.entry("경기도","41"),
            Map.entry("강원특별자치도","42"),
            Map.entry("충청북도","43"),
            Map.entry("충청남도","44"),
            Map.entry("전라북도","45"),
            Map.entry("전라남도","46"),
            Map.entry("경상북도","47"),
            Map.entry("경상남도","48"),
            Map.entry("제주특별자치도","50")
    );

    @Override
    public void run(String... args) throws Exception {
        Path csv = Paths.get("src/main/resources/db/camps.csv");
        if (!Files.exists(csv)) {
            log.warn("CSV not found: {}", csv.toAbsolutePath());
            return;
        }

        log.info("▶ seeding camp_sites …");
        try (CSVReader r = new CSVReader(new FileReader(csv.toFile()))) {
            r.skip(1);                             // 헤더 건너뛰기
            List<CampSite> buffer = new ArrayList<>();
            int lineNo = 1;

            for (String[] row; (row = r.readNext()) != null; lineNo++) {
                CampCsvParser.parse(row, lineNo).ifPresent(buffer::add);
                if (buffer.size() == 500) flush(buffer);
            }
            flush(buffer);
        }
        log.info("✅ camp_sites rows = {}", repo.count().block());
    }

    private void flush(List<CampSite> batch) {
        if (!batch.isEmpty()) {
            Flux.fromIterable(batch)
                    .concatMap(repo::upsert)   // ← saveAll 대신 upsert
                    .then()
                    .block();
            batch.clear();
        }
    }



}
