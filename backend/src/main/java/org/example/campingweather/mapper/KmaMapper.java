package org.example.campingweather.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.example.campingweather.domain.WeatherRaw;
import org.example.campingweather.dto.KmaDto;
import org.springframework.stereotype.Component;

@Component
public class KmaMapper {

    public List<WeatherRaw> toEntities(Long campId, KmaDto dto) {

        return dto.response()
                  .body()
                  .items()
                  .item()
                  .stream()                                  // Stream<KmaDto.Item>
                  .map(i -> WeatherRaw.builder()
                          .campId(campId)
                          .baseDate(i.baseDate())
                          .baseTime(i.baseTime())
                          .fcstDate(i.fcstDate())
                          .fcstTime(i.fcstTime())
                          .category(i.category())
                          .fcstValue(i.fcstValue())
                          .nx((double) i.nx())
                          .ny((double) i.ny())
                          .build())
                  .collect(Collectors.toList());
    }
}
