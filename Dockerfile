FROM gradle:8.14.3-jdk17 AS builder
USER root
COPY . .
RUN rm -rf /home/gradle/.gradle/caches/ && gradle --no-daemon build --refresh-dependencies --stacktrace


FROM eclipse-temurin:17-jre
ENV JAVA_TOOL_OPTIONS="-XX:+ExitOnOutOfMemoryError"
COPY --from=builder /home/gradle/build/libs/fint-utdanning-larling-adapter-*.jar /data/app.jar
ENTRYPOINT ["java", "-jar", "/data/app.jar"]