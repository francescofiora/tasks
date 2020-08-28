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
- JMX

# Getting Started
### Using Docker to simplify development
The purpose of this tutorial is a Producer consumer microservice, however I have inserted and a Dockerfile for MySql and phpMyAdmin + ActiveMQ Artemis + MongoDb.

### Create artemis-debian image
 - Download Apache Artemis (https://activemq.apache.org/components/artemis/download/)
    tar xvzf apache-artemis-2.14.0-bin.tar.gz
    apache-artemis-2.14.0
 - Download files from (https://github.com/apache/activemq-artemis/tree/master/artemis-docker)
    ./prepare-docker.sh apache-artemis-2.14.0
    cd apache-artemis-2.14.0
    chmod +x docker/docker-run.sh
    docker build -f ./docker/Dockerfile-debian -t artemis-debian .
 - Check image created
    docker image ls artemis-debian

### Compile
    ./gradlew clean build

Check Reports available on task-api/build/reports/checkstyle/main.html and task-executor/build/reports/checkstyle/main.html
Tests Reports available on task-api/build/reports/tests/test/index.html and task-executor/build/reports/tests/test/index.html

### Create self-aigned certificates
    cd docker
    ./create_certificate.sh

### Hot to execute applications
    docker-compose -f docker/app.yml up
    java -jar task-api/build/libs/task-api-1.0-SNAPSHOT.jar
    java -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

 - http://localhost:8081/tasks-api/swagger-ui.html
 - http://localhost:8082/tasks-executor/swagger-ui/index.html
 - http://localhost:8080/

### Debug Support
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

### How to execute with JMX support
    java -Dcom.sun.management.jmxremote.port=9999 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false  -jar task-api/build/libs/task-api-1.0-SNAPSHOT.jar
    java -Dcom.sun.management.jmxremote.port=9998 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false  -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

 - service:jmx:rmi:///jndi/rmi://localhost:9999/jmxrmi
 - service:jmx:rmi:///jndi/rmi://localhost:9998/jmxrmi

# Technologies used
- [Gradle 6.3](https://gradle.org/)
- [Java 8](http://www.oracle.com/technetwork/java/javaee/overview/index.html)
- [Spring Boot 2.3](https://spring.io/projects/spring-boot)
- [Spring Batch 4.2](https://spring.io/projects/spring-batch)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.3](https://mapstruct.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Spring Data MongoDb](https://spring.io/projects/spring-data-mongodb)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [Spring JMS](https://spring.io/guides/gs/messaging-jms/)
- [HsqlDb](http://hsqldb.org/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
- [JUnit 5](https://junit.org/junit5/)
