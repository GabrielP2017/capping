package org.example.campingweather.controller;

import org.example.campingweather.service.WeatherIngestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dev")
public class DevController {

    private final WeatherIngestService ingestService;

    public DevController(WeatherIngestService ingestService) {
        this.ingestService = ingestService;
    }

    @PostMapping("/run-one")
    public ResponseEntity<Void> runOne() {
        ingestService.ingestOne(1L);
        return ResponseEntity.noContent().build();
    }
}
