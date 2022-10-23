package it.francescofiora.tasks.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescofiora.tasks.itt.container.SpringAplicationContainer;
import it.francescofiora.tasks.itt.container.StartStopContainers;
import it.francescofiora.tasks.itt.util.ContainerGenerator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
class ApiTest extends AbstractTestContainer {

  private static final String BROKER_URL = "tcp://tasks-activemq:61616";
  private static final String DATASOURCE_URL = "jdbc:mysql://tasks-mysql:3306/tasks";
  private static final String MONGODB_URI = "mongodb://root:secret@tasks-mongodb:27017";
  private static final String EUREKA_SERVER = "task-eureka";
  private static final String EUREKA_URI = "http://user:password@task-eureka:8761/eureka";

  private static SpringAplicationContainer taskExecutor;
  private static SpringAplicationContainer taskApi;

  private static StartStopContainers containers = new StartStopContainers();

  private static ContainerGenerator containerGenerator = new ContainerGenerator(false);

  @BeforeAll
  public static void init() {
    var mySqlContainer = containerGenerator.createMySqlContainer();
    containers.add(mySqlContainer);

    var artemis = containerGenerator.createArtemisContainer();
    containers.add(artemis);

    var mongoDbContainer = containerGenerator.createMongoDbContainer();
    containers.add(mongoDbContainer);

    // @formatter:off
    var eureka = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-eureka")
        .withEnv("EUREKA_SERVER", EUREKA_SERVER)
        .withNetworkAliases(EUREKA_SERVER)
        .withExposedPorts(8761);
    // @formatter:on
    containers.add(eureka);

    // @formatter:off
    taskExecutor = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-executor")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withEnv("EUREKA_URI", EUREKA_URI)
        .withUsername(USER)
        .withPassword(PASSWORD)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8082);
    // @formatter:on
    containers.add(taskExecutor);

    // @formatter:off
    taskApi = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-api")
        .withEnv("MONGODB_URI", MONGODB_URI)
        .withEnv("BROKER_URL", BROKER_URL)
        .withUsername(USER)
        .withPassword(PASSWORD)
        .withEnv("EUREKA_URI", EUREKA_URI)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8081);
    // @formatter:on
    containers.add(taskApi);
  }

  @Test
  void testHealth() throws Exception {
    assertTrue(containers.areRunning());

    var result = taskApi.performGet(HEALTH_URI);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).contains("UP");

    result = taskExecutor.performGet(HEALTH_URI);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).contains("UP");
  }

  @Test
  void test() throws Exception {
    Map<Long, String> tasks = new HashMap<>();

    for (var i = 0; i < 10; i++) {
      var description = "description" + i;
      var json = "{ \"description\": \"" + description + "\", " + " \"type\": \"SHORT\", "
          + "\"parameters\": [ { \"name\": \"name" + i + "\",  \"value\": \"value\"  } ] }";

      var id = taskApi.createAndReturnId(TASKS_API_URI, json);
      log.debug("id = " + id);
      tasks.put(id, description);
    }

    var result = taskApi.performGet(TASKS_API_URI, Pageable.ofSize(1));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    for (var i = 0; i < 10; i++) {
      assertThat(result.getBody()).contains("\"description" + i + "\"");
    }

    var chek = new HashSet<>(tasks.keySet());
    var max = 50;

    while (!chek.isEmpty() && max > 0) {
      max--;
      for (Iterator<Long> itr = chek.iterator(); itr.hasNext();) {
        var id = itr.next();
        var tasksIdUri = String.format(TASKS_API_ID_URI, id);
        result = taskApi.performGet(tasksIdUri);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).contains("\"" + tasks.get(id) + "\"");

        if (!result.getBody().contains("\"status\":\"SCHEDULATED\"")) {
          itr.remove();
        }
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }

    result = taskExecutor.performGet(TASKS_EXECUTOR_URI, Pageable.ofSize(1));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    log.debug("Body = " + result.getBody());

    for (var entry : tasks.entrySet()) {
      var tasksIdUri = String.format(TASKS_EXECUTOR_ID_URI, entry.getKey());
      result = taskExecutor.performGet(tasksIdUri);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody())
          .contains("\"name\":\"taskRef\",\"value\":\"" + entry.getKey() + "\"");
      assertThat(result.getBody()).contains("\"status\":\"TERMINATED\"");
    }
  }

  @AfterAll
  public static void endAll() {
    containers.stopAndCloseAll();
  }
}
