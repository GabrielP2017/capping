package org.example.campingweather.wildfire;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor         // repo 주입용 생성자 자동
public class WildfireService {
    private final WildfireRepository repo;

    /** 레코드 없으면 404 던지기 */
    public Mono<WildfireResponse> findLatest(double lat, double lon) {
        return repo.findLatest(lat, lon)
                .map(WildfireResponse::from)
                .switchIfEmpty(Mono.error(
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "최근 1시간 내 데이터 없음")));
    }

}
