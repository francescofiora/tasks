# Tasks
Producer consumer microservice tutorial with Spring Boot and ActiveMQ Artemis.

### Topics covered
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
- JMX-HTTP

# Getting Started
### Compile
    ./gradlew clean build

### Dependency-Check
    ./gradlew dependencyCheckAnalyze --info

### Pitest
    ./gradlew pitest

### SonarQube
Run SonarQube

    docker-compose -f docker_dev/docker-compose-sonar.yml up

 - Sonarqube (http://localhost:9000/)

For SonarQube configuration follow this link: [Try Out SonarQube](https://docs.sonarqube.org/latest/setup/get-started-2-minutes/)

Scan project

    ./gradlew sonarqube \
    -Dsonar.projectKey=yourProjectKey \
    -Dsonar.login=yourAuthenticationToken

### Create artemis-debian image
 - Download Apache Artemis (https://activemq.apache.org/components/artemis/download/)
    tar xvzf apache-artemis-2.20.0-bin.tar.gz apache-artemis-2.20.0
 - Download files from (https://github.com/apache/activemq-artemis/tree/master/artemis-docker)

    ./prepare-docker.sh apache-artemis-2.20.0
    cd apache-artemis-2.20.0
    chmod +x docker/docker-run.sh
    docker build -f ./docker/Dockerfile-debian -t artemis-debian .

 - Check image created

    docker image ls artemis-debian

### Using Docker for tests
There is a docker compose file to run MySql, phpMyAdmin, ActiveMQ Artemis and MongoDb.

    docker-compose -f docker_dev/docker-compose.yml up

### Execute applications
    java -jar task-api/build/libs/task-api-1.0-SNAPSHOT.jar
    java -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

**Links**
 - http://localhost:8081/swagger-ui.html (Tasks-Api Swagger)
 - http://localhost:8081/actuator (Tasks-Api /info,/health,/jolokia)
 - http://localhost:8082/swagger-ui.html (Tasks-Executor Swagger)
 - http://localhost:8082/actuator (Tasks-Executor /info,/health,/jolokia)
 - http://localhost:8080/ (PhpMyAdmin)
 - http://localhost:8085/ (Mongo Express)
 - http://localhost:8161/console/login (ActiveMQ)
 - http://localhost:9000/ (Sonarqube)

### Debug Support
java -agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=y -jar task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar

## System Integration Test environment
Environment with SSL connection.

### Create docker images
    ./gradlew jibDockerBuild

### Create self-signed certificates
    cd docker
    ./create_all_certificates.sh

### Execute all applications with docker

    docker-compose -f docker/docker-compose.yml up

**Links**
 - https://localhost:8081/swagger-ui.html (Tasks-Api Swagger)
 - https://localhost:8081/actuator (Tasks-Api /info,/health,/jolokia)
 - https://localhost:8082/swagger-ui.html (Tasks-Executor Swagger)
 - https://localhost:8082/actuator (Tasks-Executor /info,/health,/jolokia)
 - http://localhost:8080/ (PhpMyAdmin)
 - https://localhost:8161/console/login (ActiveMQ)

### Reports
    task-api/build/reports/checkstyle/main.html
    task-api/build/reports/checkstyle/test.html
    task-api/build/reports/tests/test/index.html
    task-api/build/reports/jacoco/test/html/index.html
    task-api/build/reports/dependency-check-report.html
    task-api/build/reports/pitest/index.html
    task-executor/build/reports/checkstyle/main.html
    task-executor/build/reports/checkstyle/test.html
    task-executor/build/reports/tests/test/index.html
    task-executor/build/reports/jacoco/test/html/index.html
    task-executor/build/reports/dependency-check-report.html
    task-executor/build/reports/pitest/index.html
    task-message/build/reports/checkstyle/main.html
    task-message/build/reports/checkstyle/test.html
    task-message/build/reports/tests/test/index.html
    task-message/build/reports/jacoco/test/html/index.html
    task-message/build/reports/dependency-check-report.html
    task-message/build/reports/pitest/index.html
    

# Technologies used
- [Gradle 7.0](https://gradle.org/)
- [Java 11](https://openjdk.java.net/projects/jdk/11/)
- [Spring Boot 2.6](https://spring.io/projects/spring-boot)
- [Spring Batch 4.2](https://spring.io/projects/spring-batch)
- [Spring Security](https://spring.io/projects/spring-security)
- [Swagger OpeApi 3.0](https://swagger.io/specification/)
- [Mapstruct 1.4](https://mapstruct.org/)
- [Lombok 1.18](https://projectlombok.org/)
- [Spring Data JPA](https://projects.spring.io/spring-data-jpa)
- [Spring Data MongoDb](https://spring.io/projects/spring-data-mongodb)
- [Mysql connector 8.0](https://www.mysql.com/products/connector/)
- [Spring JMS](https://spring.io/guides/gs/messaging-jms/)
- [HsqlDb](http://hsqldb.org/)
- [LogBack 1.2](https://logback.qos.ch/)
- [Mockito](https://site.mockito.org/)
- [JUnit 5](https://junit.org/junit5/)
- [OpenPojo 0.9](https://github.com/OpenPojo)
- [CheckStyle 8.44](https://checkstyle.sourceforge.io/)
- [Owasp Dependency Check 6.2](https://owasp.org/www-project-dependency-check/)
- [Jacoco 0.8](https://www.jacoco.org/)
- [Pitest 1.7](https://pitest.org/)
- [Jolokia 1.7](https://jolokia.org/)
