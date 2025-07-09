package org.example.campingweather.wildfire;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/wildfire")
@RequiredArgsConstructor
@Validated                    // @Min/@Max 유효성 켜기
public class WildfireController {

    private final WildfireService service;

    @GetMapping
    public Mono<WildfireResponse> latest(
            @RequestParam @Min(-90)  @Max(90)  double lat,
            @RequestParam @Min(-180) @Max(180) double lon) {
        return service.findLatest(lat, lon);
    }

}
