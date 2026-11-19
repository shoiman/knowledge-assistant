# ---Step 1: base of project (Maven) ---

FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /knowledge-assistant

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q package -DskipTests

# ---Step 2: runtime ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /knowledge-assistant

RUN addgroup -S app && adduser -S app -G app
COPY --from=builder /knowledge-assistant/target/*.jar app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "/knowledge-assistant/app.jar"]