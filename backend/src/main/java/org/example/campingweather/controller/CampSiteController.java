package org.example.campingweather.controller;

import lombok.RequiredArgsConstructor;
import org.example.campingweather.domain.CampSite;
import org.example.campingweather.repository.CampSiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campsites")
@RequiredArgsConstructor
public class CampSiteController {

    private final CampSiteRepository repo;

    /** 전체 리스트 – 프런트에서 page, size 쿼리로 호출 */
    @GetMapping
    public Page<CampSite> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3000") int size) {
        return repo.findAll(PageRequest.of(page, size));
    }
}