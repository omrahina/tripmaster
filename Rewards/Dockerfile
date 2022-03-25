FROM openjdk:8-jdk
LABEL "maintainer"="omrave"
COPY target/*.jar rewards-0.0.1-SNAPSHOT.jar
EXPOSE 8091
ENTRYPOINT ["java","-jar","/rewards-0.0.1-SNAPSHOT.jar"]