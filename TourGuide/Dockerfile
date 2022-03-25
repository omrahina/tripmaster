FROM openjdk:8-jdk
LABEL "maintainer"="omrave"
COPY target/*.jar tourguide-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/tourguide-0.0.1-SNAPSHOT.jar"]