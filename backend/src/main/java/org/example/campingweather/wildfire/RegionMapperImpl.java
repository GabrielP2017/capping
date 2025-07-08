package org.example.campingweather.wildfire;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.wildfire.RegionMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("db")                         // ★ 프로필 'db' 일 때만 활성
@RequiredArgsConstructor
public class RegionMapperImpl implements RegionMapper {

    private final RegionGeoRepository repo;

    @Override
    public List<String> allRegionCodes() {
        // DB에서 코드만 전부 조회
        return repo.findAll()
                .stream()
                .map(RegionGeo::getCode)
                .toList();
    }

    @Override
    public LatLon toLatLon(String code) {
        RegionGeo g = repo.findById(code)
                .orElseThrow(() -> new IllegalArgumentException("코드 없음: " + code));
        return new LatLon(g.getLat(), g.getLon());
    }
}
