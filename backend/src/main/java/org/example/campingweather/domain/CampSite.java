package org.example.campingweather.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity                      // JPA 엔티티 선언
@Table(name = "camp_sites")  // DB 테이블명
@Getter @Setter              // Lombok: 게터·세터 자동
public class CampSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;          // 캠핑장 이름
    private String address;       // 주소
    private BigDecimal lat;       // 위도
    private BigDecimal lon;       // 경도
    private String phone;         // 전화번호

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
