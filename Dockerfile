# build for build
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

COPY gradlew .
COPY gradle/ gradle/
COPY settings.gradle.kts .
COPY build.gradle.kts .
COPY buildSrc/ buildSrc/

COPY core/build.gradle.kts core/
COPY utils/build.gradle.kts utils/

COPY core/src/ core/src/
COPY utils/src/ utils/src/

RUN chmod +x gradlew

RUN ./gradlew clean build --no-daemon -x test && \
    find core/build/libs -name "*.jar" -exec mv {} app.jar \;

# actuall build
FROM openjdk:17-jre-slim AS runner

WORKDIR /app

# copy from previous thing
COPY --from=builder /app/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]