package org.example.campingweather.controller;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.scheduler.WeatherScheduler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actuator")
@RequiredArgsConstructor
public class WeatherSchedulerEndpoint {
    private final WeatherScheduler scheduler;

    /** 수동 데이터 수집 */
    @PostMapping("/refresh-weather")
    public void refresh() {
        scheduler.fetch();
    }
}
