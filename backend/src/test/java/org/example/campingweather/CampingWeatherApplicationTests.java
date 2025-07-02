package org.example.campingweather;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
class CampingWeatherApplicationTests {

    // ① 테스트 전용 Postgres 컨테이너 정의
    @Container
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>(DockerImageName.parse("postgis/postgis:15-3.4").asCompatibleSubstituteFor("postgres"))
        .withDatabaseName("testdb")
        .withUsername("postgres")
        .withPassword("postgres");

    // ② Spring DataSource·Flyway 프로퍼티 동적 주입
    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);

        r.add("spring.flyway.url", postgres::getJdbcUrl);
        r.add("spring.flyway.user", postgres::getUsername);
        r.add("spring.flyway.password", postgres::getPassword);
    }

    @Test
    void contextLoads() {
        // 빈 로딩 + Flyway 마이그레이션 + JPA 스캔이 모두 성공하면 통과
    }
}
