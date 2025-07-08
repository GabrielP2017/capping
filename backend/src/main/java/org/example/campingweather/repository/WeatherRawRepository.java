package org.example.campingweather.repository;

import org.example.campingweather.domain.WeatherRaw;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRawRepository extends JpaRepository<WeatherRaw, Long> {
}