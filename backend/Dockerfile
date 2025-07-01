# ① 자바 런타임 이미지 (Alpine: 용량 작음)
FROM bellsoft/liberica-openjdk-alpine:21

# ② 작업 디렉토리
WORKDIR /app

# ③ jar 복사 (★ → 이름에 와일드카드 사용)
COPY build/libs/camping-weather-0.0.1-SNAPSHOT.jar app.jar

# ④ 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]
