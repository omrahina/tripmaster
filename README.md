# TourGuide

TourGuide is a trip oriented application with features such as tracking, trip deals, nearby attractions and many more.

## Installation

### Prerequisites

- Java [8+](https://adoptopenjdk.net/?variant=openjdk8&jvmVariant=hotspot)
- Maven [3.6+](https://maven.apache.org/download.cgi)
- Docker 17.03.1-ce-rc1 +
- docker-compose version 1.11.2

### Run

Build the project

```bash
mvn clean install
```

Build docker images for each service and then execute

```bash
docker-compose up
```
Note that image names can be found in the [docker-compose](docker-compose.yml) file.

To generate test reports execute the following command

```bash
mvn site
```

## Test
[A Postman Collection](Trip%20Master.postman_collection.json) is provided for testing purposes.
