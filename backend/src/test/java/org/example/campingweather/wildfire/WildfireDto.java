package org.example.campingweather.wildfire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data   // getter‧setter‧toString 등 자동 생성
public class WildfireDto {

    private String regionCode;
    private int riskLevel;

    // ────────── Getter / Setter 직접 작성 ──────────
    public String getRegionCode() {
        return regionCode;
    }
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public int getRiskLevel() {
        return riskLevel;
    }
    public void setRiskLevel(int riskLevel) {
        this.riskLevel = riskLevel;
    }

}
