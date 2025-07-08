package org.example.campingweather.wildfire;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "wildfire_risks")
@Getter @Setter
public class RegionGeo {
    @Id
    private String code;     // 11110
    private double lat;
    private double lon;
}
