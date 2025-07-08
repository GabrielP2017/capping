package org.example.campingweather;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.example.campingweather.wildfire.WildfireApiClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")   // application-test.yml을 사용하도록 지정
class CampingWeatherApplicationTests {

    static DockerImageName TS_IMAGE =
            DockerImageName.parse("timescale/timescaledb-ha:pg16-all")
                    .asCompatibleSubstituteFor("postgres");

    // 1) 컨테이너 정의 (포트 충돌 우회 예시)
    @Container
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(TS_IMAGE)
            .withDatabaseName("campdb")
            .withUsername("postgres")
            .withPassword("camp")
            .withReuse(false);

    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
        r.add("spring.flyway.url",          pg::getJdbcUrl);      // ★ 추가
        r.add("spring.flyway.user",         pg::getUsername);
        r.add("spring.flyway.password",     pg::getPassword);
    }


    // 3) Docker 확인 안 되면 테스트 Skip
    static {
        boolean dockerUp = DockerClientFactory.instance().isDockerAvailable();
        Assumptions.assumeTrue(dockerUp, "Docker daemon unavailable → tests skipped");
    }

    @Test
    void contextLoads() {
    }

    /** ① Mock WebServer */
    static MockWebServer mockWebServer;

    /** ② 테스트 대상 Bean */
    @Autowired
    WildfireApiClient client;

    /** ③ 애플리케이션 프로퍼티를 mock URL로 덮어쓰기 */
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry reg) throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start(0);                                   // 랜덤 포트
        reg.add("WILDFIRE_API_URL", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void fetchRisk_parsesRiskLevel() throws JsonProcessingException {
        // ④ Mock 응답 JSON – 위험도 3
        String body = """
            {
              "response": {
                "body": {
                  "items": {
                    "item": [
                      { "sigunguCd": "11110", "riskLevel": 3, "analdate": "2025070810" }
                    ]
                  }
                }
              }
            }
            """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        // ⑤ 검증
        StepVerifier.create(client.fetchRisk("11110"))
                .assertNext(dto -> {
                    Assertions.assertThat(dto.getRiskLevel()).isEqualTo(3);
                    Assertions.assertThat(dto.getRegionCode()).isEqualTo("11110");
                })
                .verifyComplete();
    }



}
