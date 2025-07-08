package org.example.campingweather.wildfire;

import java.util.List;

public interface RegionMapper {
    /** 시군구 코드 전부 */
    List<String> allRegionCodes();
    LatLon toLatLon(String code);           // 코드 ➜ 좌표
}