package org.example.campingweather.seed;

import org.example.campingweather.domain.CampSite;
import org.example.campingweather.repository.CampSiteRepository;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.FileReader;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class CampCsvSeeder implements CommandLineRunner {
    private final CampSiteRepository repo;

    @Override
    public void run(String... args) throws Exception {
        Path p = Paths.get("src/main/resources/db/camps.csv");

        if (!Files.exists(p)) {
            System.out.println("CSV 없음, 시드 skipped: " + p.toAbsolutePath());
            return;
        }

        try (CSVReader r = new CSVReader(new FileReader(p.toFile()))) {
            r.skip(1);                       // 헤더
            List<CampSite> batch = new ArrayList<>();
            String[] row;

            while ((row = r.readNext()) != null) {

                // 1) 컬럼 수 확인 (최소 위경도 포함 4칸)
                if (row.length < 4) continue;

                // 2) 위·경도 값 공백 체크
                if (row[2].isBlank() || row[3].isBlank()) continue;

                CampSite c = new CampSite();
                c.setName(row[0].trim());
                c.setAddress(row[1].trim());
                c.setLat(new BigDecimal(row[2].trim()));
                c.setLon(new BigDecimal(row[3].trim()));
                if (row.length > 4) c.setPhone(row[4].trim());

                batch.add(c);
                if (batch.size() == 1000) {
                    repo.saveAll(batch);
                    batch.clear();
                }
            }
            repo.saveAll(batch);
            System.out.println("✅ camp_sites rows = " + repo.count());
        }

    }
}

