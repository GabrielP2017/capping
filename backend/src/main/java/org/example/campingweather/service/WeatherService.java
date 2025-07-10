package org.example.campingweather.service;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.campingweather.repository.WeatherRawRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRawRepository repo;

    /** 최신 TMP·POP 반환 */
    public Map<String, String> getCurrent(long campId) {
        return repo.findLatest(campId, PageRequest.of(0, 2)).stream()
                   .collect(Collectors.toMap(m -> m.get("cat"), m -> m.get("val")));
    }
}
