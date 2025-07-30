package org.example.campingweather.controller;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.repository.WildfireRepository;
import org.example.campingweather.wildfire.WildfireEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/wildfire")
@RequiredArgsConstructor
public class WildfireController {

    private final WildfireRepository repo;      // 🔸 다시 Repository 주입

    /** regionCode 로 최신 위험도 반환 */
    @GetMapping
    public Mono<WildfireEntity> get(@RequestParam String regionCode) {
        return repo.findTopByRegionCodeOrderByFetchedAtDesc(regionCode)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No data for " + regionCode)));
    }

}
