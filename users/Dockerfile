FROM openjdk:8-jdk
LABEL "maintainer"="omrave"
COPY target/*.jar users-0.0.1-SNAPSHOT.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/users-0.0.1-SNAPSHOT.jar"]

#Not for now cause Docker build
#FROM maven:3.6.1-jdk-8-slim AS build
#RUN mkdir -p /workspace
#WORKDIR /workspace
#COPY pom.xml /workspace
#COPY src /workspace/src
#RUN mvn -B -f pom.xml clean package -DskipTests
#FROM openjdk:8-jdk-slim
#COPY --from=build /workspace/target/*.jar app.jar
#EXPOSE 8081
#ENTRYPOINT ["java","-jar","app.jar"]