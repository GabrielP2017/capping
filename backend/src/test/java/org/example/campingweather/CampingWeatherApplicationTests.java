package org.example.campingweather;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")   // application-test.yml을 사용하도록 지정
class CampingWeatherApplicationTests {

    @Test
    void contextLoads() {
    }

}
