# Tasks
Producer consumer microservice tutorial with Spring Boot and ActiveMQ Artemis.

# Topics covered
- Spring Boot Rest Api
- Spring Boot Batch
- SSL connections
- JMS(Java Message Service)
- Swagger UI for visualizing APIs
- Error Handling
- Basic Authentication
- Mapper for POJO<->DTO 
- Logging
- Testing
    - Repositories using DataJpaTest
    - Repositories using DataMongoTest
    - Services using Mockito
    - EndPoints using WebMvcTest
    - Pojos and Dtos using OpenPojo
- JMX

# Getting Started
### Using Docker to simplify development
The purpose of this tutorial is a Producer consumer microservice, however I have added a Dockerfile for MySql + phpMyAdmin + ActiveMQ Artemis + MongoDb.

### Compile
    ./gradlew clean build

Check Reports available on task-api/build/reports/checkstyle/main.html and task-executor/build/reports/checkstyle/main.html
Tests Reports available on task-api/build/reports/tests/test/index.html and task-executor/build/reports/tests/test/index.html

### Create artemis-debian image
 - Download Apache Artemis (https://activemq.apache.org/components/artemis/download/)
    tar xvzf apache-artemis-2.17.0-bin.tar.gz apache-artemis-2.17.0
 - Download files from (https://github.com/apache/activemq-artemis/tree/master/artemis-docker)
    ./prepare-docker.sh apache-artemis-2.17.0
    cd apache-artemis-2.17.0
    chmod +x docker/docker-run.sh
    docker build -f ./docker/Dockerfile-debian -t artemis-debian .
 - Check image created
    docker image ls artemis-debian

## Dev environment
Basic environment for development with dev profile.

## compile
    ./gradlew clean build

### Hot to execute applications
    docker-compose -f docker_dev/docker-compose.yml up
    java -jar task-api/build/libs/task-api-1.0-SNAPSHOT.jar
    java -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

it could be possible run applications using Eclipse 

 - http://localhost:8081/tasks-api/swagger-ui.html
 - http://localhost:8082/tasks-executor/swagger-ui.html
 - http://localhost:8080/
 - http://localhost:8161/console/login

### Debug Support
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

## System Integration Test environment
Environment with sit profile and SSL connection.

### Create self-signed certificates and prepare environment
    cd docker
    ./create_all_certificates.sh
    ./cp_jars.sh

### Hot to execute applications
    docker-compose -f docker/docker-compose.yml up

 - https://localhost:8081/tasks-api/swagger-ui.html (Tasks-Api)
 - https://localhost:8082/tasks-executor/swagger-ui.html (Tasks-Executor)
 - http://localhost:8080/ (PhpMyAdmin)
 - https://localhost:8161/console/login (ActiveMQ)

 - service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi
 - service:jmx:rmi:///jndi/rmi://localhost:9998/jmxrmi

## reports
    task-api/build/reports/checkstyle/main.html
    task-api/build/reports/checkstyle/test.html
    task-api/build/reports/tests/test/index.html
    task-api/build/reports/jacoco/test/html/index.html
    task-executor/build/reports/checkstyle/main.html
    task-executor/build/reports/checkstyle/test.html
    task-executor/build/reports/tests/test/index.html
    task-executor/build/reports/jacoco/test/html/index.html
    task-message/build/reports/checkstyle/main.html
    task-message/build/reports/checkstyle/test.html
    task-message/build/reports/tests/test/index.html
    task-message/build/reports/jacoco/test/html/index.html
    

# Technologies used
- [Gradle 7.0](https://gradle.org/)
- [Java 11](https://openjdk.java.net/projects/jdk/11/)
- [Spring Boot 2.5](https://spring.io/projects/spring-boot)
- [Spring Batch 4.2](https://spring.io/projects/spring-batch)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.3](https://mapstruct.org/)
- [Lombok 1.18](https://projectlombok.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Spring Data MongoDb](https://spring.io/projects/spring-data-mongodb)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [Spring JMS](https://spring.io/guides/gs/messaging-jms/)
- [HsqlDb](http://hsqldb.org/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
- [JUnit 5](https://junit.org/junit5/)
- [OpenPojo 0.8](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io/)
- [Jacoco 0.8](https://www.jacoco.org/)
