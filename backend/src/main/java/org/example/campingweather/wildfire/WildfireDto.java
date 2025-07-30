package org.example.campingweather.wildfire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor          // 🔸 두 필드용 생성자 자동 생성
@JsonIgnoreProperties(ignoreUnknown = true)
public class WildfireDto {

    private String analdate;   // "yyyy-MM-dd HH"
    private int    riskLevel;
    private String regionCode;

    /** Item → DTO  */
    public static WildfireDto from(WildfireResponse.Item i) {
        int lv = i.getD4()>0 ? 4 : i.getD3()>0 ? 3 : i.getD2()>0 ? 2 : 1;
        return new WildfireDto(i.getAnaldate(), lv, i.getRegioncode());
    }

    /** DTO → Entity  */
    public WildfireEntity toEntity() {
        LocalDateTime ts = LocalDateTime.parse(analdate,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
        return new WildfireEntity(null, regionCode, riskLevel, ts);
    }

}
