package org.example.campingweather.seed;

import lombok.extern.slf4j.Slf4j;
import org.example.campingweather.domain.CampSite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Slf4j
final class CampCsvParser {

    private CampCsvParser() {}        // 인스턴스화 금지

    // 동일 Map을 두 번 선언하기 싫다면 public static으로 옮겨 재사용해도 됨
    private static final Map<String,String> PROVINCE_MAP = CampCsvSeeder.PROVINCE_MAP;

    static Optional<CampSite> parse(String[] row, int lineNo) {
        if (row.length < 4) return Optional.empty();

        String nameRaw = row[0];
        String addrRaw = row[1];
        String latRaw  = row[2];
        String lonRaw  = row[3];

        if (latRaw == null || latRaw.isBlank()
                || lonRaw == null || lonRaw.isBlank()
                || addrRaw == null || addrRaw.isBlank()) {
            log.warn("⏩  CSV line {} skipped – blank required field", lineNo);
            return Optional.empty();
        }

        try {
            BigDecimal lat = new BigDecimal(latRaw.trim()).setScale(6, RoundingMode.HALF_UP);
            BigDecimal lon = new BigDecimal(lonRaw.trim()).setScale(6, RoundingMode.HALF_UP);

            String province = addrRaw.trim().split(" ")[0];
            String code     = PROVINCE_MAP.getOrDefault(province, "41");

            CampSite c = new CampSite();
            c.setName(nameRaw.trim());
            c.setAddress(addrRaw.trim());
            c.setLat(lat);
            c.setLon(lon);
            c.setRegionCode(code);

            if (row.length > 4 && row[4] != null && !row[4].isBlank()) {
                c.setPhone(row[4].trim());
            }
            return Optional.of(c);

        } catch (NumberFormatException ex) {
            log.warn("⏩  CSV line {} skipped – bad number: {}", lineNo, ex.getMessage());
            return Optional.empty();
        }
    }
}