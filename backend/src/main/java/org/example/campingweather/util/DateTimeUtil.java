package org.example.campingweather.util;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    public static LocalDateTime nowKst() {
        return LocalDateTime.now(KST);
    }
    /** 최근 정시(또는 n 시간 간격)로 내림 반올림 */
    public static LocalDateTime alignToHour(LocalDateTime dt, int hourStep) {
        return dt.truncatedTo(ChronoUnit.HOURS)
                 .minusHours(dt.getHour() % hourStep);
    }
}
