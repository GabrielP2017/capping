package org.example.campingweather.wildfire;

public record WildfireResponse(String regionCode, int riskLevel, java.time.LocalDateTime fetchedAt) {

/** Entity → DTO 변환 헬퍼 */
public static WildfireResponse from(WildfireEntity e) {
    return new WildfireResponse(
            e.getRegionCode(),
            e.getRiskLevel(),
            e.getFetchedAt()
    );
}


}
