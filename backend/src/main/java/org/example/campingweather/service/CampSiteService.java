package org.example.campingweather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.campingweather.domain.CampSite;
import org.example.campingweather.repository.CampSiteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampSiteService {

    private final CampSiteRepository repo;

    public Mono<Page<CampSite>> list(Pageable pageable) {
        Flux<CampSite> rows = repo.findPage(
                pageable.getPageSize(),
                pageable.getOffset());

        Mono<Long> total = repo.countAll()
                .doOnNext(cnt ->
                        log.info("✅ camp_sites rows = {}", cnt));

        return rows.collectList()
                .zipWith(total)
                .map(t -> new PageImpl<>(t.getT1(), pageable, t.getT2()));
    }
}