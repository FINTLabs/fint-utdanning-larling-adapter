FROM gradle:8.1.1-jdk17 as builder
USER root
COPY . .
RUN rm -rf /home/gradle/.gradle/caches/ && gradle --no-daemon build --refresh-dependencies --stacktrace


FROM gcr.io/distroless/java17
ENV JAVA_TOOL_OPTIONS -XX:+ExitOnOutOfMemoryError
COPY --from=builder /home/gradle/build/libs/fint-utdanning-larling-adapter-*.jar /data/app.jar
CMD ["/data/app.jar"]