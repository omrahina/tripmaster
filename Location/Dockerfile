FROM openjdk:8-jdk
LABEL "maintainer"="omrave"
COPY target/*.jar userLocation-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/userLocation-0.0.1-SNAPSHOT.jar"]