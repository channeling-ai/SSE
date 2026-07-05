# ==============================================================================
# Build Stage — gradle로 jar 만들기
# ==============================================================================
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# 의존성만 먼저 복사 → 코드 변경만 있을 때 다운로드 캐시 재활용
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --version

# 나머지 소스 복사 후 빌드
COPY src ./src
RUN ./gradlew clean bootJar -x test --no-daemon

# ==============================================================================
# Run Stage — JRE에 jar만 얹기
# ==============================================================================
FROM eclipse-temurin:17-jre-jammy

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 애플리케이션 실행 전용 사용자 (root 금지)
RUN groupadd -g 1000 appuser && \
    useradd --no-log-init -u 1000 -g appuser -m appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appuser /app/logs

USER appuser
WORKDIR /app

COPY --from=builder --chown=appuser:appuser /app/build/libs/*.jar sse-server.jar

EXPOSE 8081

CMD ["java", "-jar", "sse-server.jar"]
