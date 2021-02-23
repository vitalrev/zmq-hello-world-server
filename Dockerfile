# Stage 0, "build-stage", based on Gradle, to build and compile the backend
FROM gradle:6.7.0-jdk11 AS build-stage

# Get all the code needed to run the app
COPY . /app/

# Default gradle user is `gradle`. We need to add permission on working directory for gradle to build.
USER root
RUN chown -R gradle:gradle /app
USER gradle

# Change directory so that our commands run inside this new directory
WORKDIR /app

# BUILD
RUN ./gradlew clean build --no-daemon --info \
    && mv build/libs/ZMQHelloServer-*.jar app.jar

USER root
RUN chown -R root:root /app

# Stage 1, based on Openjdk, to have only the compiled app, ready for production
#FROM registry.access.redhat.com/ubi8/openjdk-11
FROM openjdk:11.0-jre-slim

ARG JAVA_OPTS="-Xmx1024m -Djava.security.egd=file:/dev/./urandom"
ARG PORT=9702

ENV JAVA_OPTS ${JAVA_OPTS}
ENV SERVER_PORT ${PORT}

EXPOSE ${PORT}

# Change directory so that our commands run inside this new directory
WORKDIR /app

COPY --from=build-stage /app/app.jar .

ENTRYPOINT java ${JAVA_OPTS} -jar app.jar
