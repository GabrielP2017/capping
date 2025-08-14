package org.example.campingweather;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.example.campingweather.repository.CampSiteRepository;
import org.example.campingweather.wildfire.WildfireApiClient;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class CampingWeatherApplicationTests {

    /* ---------------- Testcontainers: Timescale + PostGIS ---------------- */
    static final DockerImageName TS_IMAGE =
            DockerImageName.parse("timescale/timescaledb-ha:pg16-all")
                    .asCompatibleSubstituteFor("postgres");

    @Container
    static final PostgreSQLContainer<?> pg =
            new PostgreSQLContainer<>(TS_IMAGE)
                    .withDatabaseName("campdb")
                    .withUsername("postgres")
                    .withPassword("camp");

    /* ---------- Datasource & Flyway 를 컨테이너 접속 정보로 덮어쓰기 ---------- */
    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);

        r.add("spring.flyway.url",      pg::getJdbcUrl);
        r.add("spring.flyway.user",     pg::getUsername);
        r.add("spring.flyway.password", pg::getPassword);
    }

    /* ---------------- Docker 데몬 없으면 테스트 전체 Skip ---------------- */
    static {
        Assumptions.assumeTrue(
                DockerClientFactory.instance().isDockerAvailable(),
                "Docker daemon unavailable → tests skipped");
    }

    /* ---------------- 외부 산림청 API 를 MockWebServer 로 대체 ------------- */
    static MockWebServer mockWebServer;

    @DynamicPropertySource
    static void overrideApiUrl(DynamicPropertyRegistry reg) throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        reg.add("WILDFIRE_API_URL", () -> mockWebServer.url("/").toString());
    }

    @AfterAll
    static void shutdown() throws Exception {
        if (mockWebServer != null) mockWebServer.shutdown();
    }
    /* ====== 2) CampSiteRepository를 Mockito 목으로 등록 ===== */
    @TestConfiguration
    static class StubConfig {
        @Bean
        CampSiteRepository campSiteRepository() {
            return Mockito.mock(CampSiteRepository.class);
        }
        @Bean
        WildfireApiClient wildfireApiClient() {
            WildfireApiClient mock = Mockito.mock(WildfireApiClient.class);
            // 아무 값이나 리턴하도록 기본 스텁
            Mockito.when(mock.fetchRisk(Mockito.anyString()))
                    .thenReturn(Mono.empty());
            return mock;
        }

    }

    /* ---------------- 실제 Bean 주입 받아 검증 ---------------- */
    @Autowired WildfireApiClient client;

    @Test
    void contextLoads() { }

    @Test
    void fetchRisk_parsesRiskLevel() throws JsonProcessingException {
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
            }""";
        mockWebServer.enqueue(new MockResponse()
                .setBody(body)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(client.fetchRisk("11110"))
                .assertNext(dto -> {
                    Assertions.assertThat(dto.getRiskLevel()).isEqualTo(3);
                    Assertions.assertThat(dto.getRegionCode()).isEqualTo("11110");
                })
                .verifyComplete();
    }

}
