package org.example.campingweather.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@Table("camp_sites")
public class CampSite {

    @Id                    // ← 이것만 있으면 됩니다
    private Long id;
    @Column("name")
    private String name;
    @Column("address")
    private String address;
    @Column("lat")
    private BigDecimal lat;
    @Column("lon")
    private BigDecimal lon;

    @Column("phone")
    private String phone;

    @Column("region_code")
    private String regionCode;

    @Column("last_updated")
    private LocalDateTime lastUpdated;

}