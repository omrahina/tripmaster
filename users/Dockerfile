FROM openjdk:8-jdk
LABEL "maintainer"="omrave"
COPY target/*.jar users-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/users-0.0.1-SNAPSHOT.jar"]