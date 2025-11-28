# Base Image - Java 17 런타임
FROM eclipse-temurin:17-jdk-jammy

# curl 설치 (필요 시 사용)
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 애플리케이션 실행 전용 사용자 생성 (보안상 root 사용 금지 권장)
RUN groupadd -g 1000 appuser && \
    useradd --no-log-init -u 1000 -g appuser -m appuser && \
    mkdir -p /app/logs && \
    chown -R appuser:appuser /app/logs

# 실행 사용자 설정
USER appuser

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 결과물 복사 (build/libs/*.jar)
COPY --chown=appuser:appuser ./build/libs/*.jar sse-server.jar

# SSE 서버 포트 노출 (예: 8081)
EXPOSE 8081

# 어플리케이션 실행
CMD ["java", "-jar", "sse-server.jar"]
