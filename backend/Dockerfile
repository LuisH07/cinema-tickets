
#Build
FROM maven:3.9.12-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

RUN mvn dependency:go-offline -q

COPY src/ src/

RUN mvn package -DskipTests -q

#Runtime

FROM eclipse-temurin:21-jre-noble AS runtime

RUN groupadd --system appgroup && useradd --system --gid appgroup appuser

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
  "-jar", "app.jar"]