# ==================== BigBang Dockerfile (多阶段构建) ====================
# Stage 1: 构建
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: 运行
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN addgroup -g 1000 appgroup && adduser -u 1000 -G appgroup -D appuser
COPY --from=builder /app/target/*.jar app.jar
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
