package it.francescofiora.tasks.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescofiora.tasks.itt.SpringAplicationContainer;
import it.francescofiora.tasks.itt.StartStopContainers;
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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
class ApiTest extends AbstractTestContainer {

  private static final String BROKER_URL = "tcp://tasks-activemq:61616";
  private static final String DATASOURCE_URL = "jdbc:mysql://tasks-mysql:3306/tasks";
  private static final String MONGODB_URI = "mongodb://root:secret@tasks-mongodb:27017";

  private static final String TASKS_API_URI = "/tasks-api/api/v1/tasks";
  private static final String TASKS_API_ID_URI = "/tasks-api/api/v1/tasks/%d";

  private static final String TASKS_EXECUTOR_URI = "/tasks-executor/api/v1/tasks";
  private static final String TASKS_EXECUTOR_ID_URI = "/tasks-executor/api/v1/tasks/%d";

  private static final String HEALTH_URI = "/actuator/health";

  private static MySQLContainer<?> mySqlContainer;
  private static GenericContainer<?> myMongoDBContainer;
  private static GenericContainer<?> myArtemis;
  private static SpringAplicationContainer taskExecutor;
  private static SpringAplicationContainer taskApi;

  private static StartStopContainers containers = new StartStopContainers();

  @BeforeAll
  public static void init() {
    mySqlContainer = createMySqlContainer();
    containers.add(mySqlContainer);

    myArtemis = createArtemisContainer();
    containers.add(myArtemis);

    myMongoDBContainer = createMongoDbContainer();
    containers.add(myMongoDBContainer);

    // @formatter:off
    taskExecutor = createSpringAplicationContainer("francescofiora-task-executor")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", "japp")
        .withEnv("DATASOURCE_ADMIN_PASSWORD", "secret")
        .withUsername("user")
        .withPassword("password")
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8082);
    // @formatter:on
    containers.add(taskExecutor);

    // @formatter:off
    taskApi = createSpringAplicationContainer("francescofiora-task-api")
        .withEnv("MONGODB_URI", MONGODB_URI)
        .withEnv("BROKER_URL", BROKER_URL)
        .withUsername("user")
        .withPassword("password")
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
