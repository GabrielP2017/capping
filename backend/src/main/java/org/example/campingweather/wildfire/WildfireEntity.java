package org.example.campingweather.wildfire;

import jakarta.persistence.*;          // JPA 기준, R2DBC면 @Table @Id 등 변경
import java.time.LocalDateTime;

@Entity
@Table(name = "wildfire_risks")
public class WildfireEntity {

    @Id @GeneratedValue
    private Long id;
    private String regionCode;
    private int riskLevel;
    private double lat;
    private double lon;
    private LocalDateTime fetchedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}
