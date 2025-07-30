package org.example.campingweather.controller;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.domain.CampSite;
import org.example.campingweather.repository.CampSiteRepository;
import org.example.campingweather.service.CampSiteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/campsites")
@RequiredArgsConstructor
public class CampSiteController {

    private final CampSiteService service;

    @GetMapping
    public Mono<Page<CampSite>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3000") int size) {
        return service.list(PageRequest.of(page, size));
    }

}
