FROM amazoncorretto:17-alpine3.20-jdk as builder
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
# Out-of-the-box, Spring Boot provides four layers we can split jar into
# The goal is to place application code and third-party libraries into layers that reflect how often they change
# For example, application code is likely what changes most frequently, so it gets its own layer.
RUN java -Djarmode=tools -jar application.jar extract --layers --launcher --destination extracted
FROM amazoncorretto:17-alpine3.20-jdk
WORKDIR /application
RUN apk update && apk add curl
COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./
# Use the Spring Boot loader to start the application
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]