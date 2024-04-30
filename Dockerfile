FROM openjdk:8-jdk-alpine
LABEL maintainer="soumyajitchakraborty735@gmail.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/CodeScreen_3yufmg21-1.0.0.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

