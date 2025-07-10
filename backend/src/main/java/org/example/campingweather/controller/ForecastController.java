package org.example.campingweather.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.campingweather.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
public class ForecastController {

    private final WeatherService svc;

    @GetMapping("/{campId}")
    public Map<String, String> get(@PathVariable long campId) {
        return svc.getCurrent(campId);
    }
}
