package org.example.campingweather.wildfire;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("wildfire_risks")     // ← R2DBC 전용 매핑
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WildfireEntity {

    @Id private Long id;           // BIGSERIAL
    private String regionCode;              // '11' …
    private int riskLevel;               // 1~4
    private LocalDateTime fetchedAt;        // 분석 시각

}
