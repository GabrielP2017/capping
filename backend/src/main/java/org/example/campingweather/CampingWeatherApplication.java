package org.example.campingweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CampingWeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampingWeatherApplication.class, args);
    }

}
