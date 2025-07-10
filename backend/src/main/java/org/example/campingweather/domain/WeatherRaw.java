package org.example.campingweather.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weather_raw")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @Builder
public class WeatherRaw {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "camp_id", nullable = false)
    private Long campId;

    private String baseDate;   // 예: 20250709
    private String baseTime;   // 예: 0200
    private Double nx;         // 격자 X
    private Double ny;         // 격자 Y
    private String category;   // TMP, REH, SKY …
    private String fcstDate;   // 예보 날짜
    private String fcstTime;   // 예보 시각
    private String fcstValue;  // 예보 값 (문자/수치 혼재)

    @Column(columnDefinition = "timestamp default now()")
    private java.time.LocalDateTime createdAt;
}
