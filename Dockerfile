FROM openjdk:11.0.13-jdk-slim

ADD target/appTC-0.0.1-SNAPSHOT.jar appTC.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "appTC.jar"]