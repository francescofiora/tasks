package it.francescofiora.tasks.itt.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Abstract class for Test Containers.
 */
public abstract class AbstractTestContainer {

  public static final String HEALTH_URI = "/actuator/health";

  public static final String TASKS_API_URI = "/tasks-api/api/v1/tasks";
  public static final String TASKS_API_ID_URI = "/tasks-api/api/v1/tasks/%d";

  public static final String TASKS_EXECUTOR_URI = "/tasks-executor/api/v1/tasks";
  public static final String TASKS_EXECUTOR_ID_URI = "/tasks-executor/api/v1/tasks/%d";

  public static final String EUREKA_APPS = "/eureka/apps";

  public static final String USER = "user";
  public static final String PASSWORD = "password";

  protected void assertUp(ResponseEntity<String> result) {
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).contains("UP");
  }
}
