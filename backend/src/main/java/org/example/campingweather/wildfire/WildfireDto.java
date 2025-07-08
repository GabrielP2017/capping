package org.example.campingweather.wildfire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data // Lombok: getter/setter toString 등 자동
@JsonIgnoreProperties(ignoreUnknown = true)
public class WildfireDto {

    @JsonProperty("sigunguCd")   // 시군구 코드
    private String regionCode;

    @JsonProperty("riskLevel")   // 위험도 0~4
    private int riskLevel;

    @JsonProperty("analdate")    // 분석 날짜(yyyyMMddHH)
    private String analyzedAt;

}
