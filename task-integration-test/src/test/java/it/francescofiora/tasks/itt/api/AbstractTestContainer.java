package it.francescofiora.tasks.itt.api;

/**
 * Abstract class for Test Containers.
 */
public class AbstractTestContainer {

  public static final String HEALTH_URI = "/actuator/health";

  public static final String TASKS_API_URI = "/tasks-api/api/v1/tasks";
  public static final String TASKS_API_ID_URI = "/tasks-api/api/v1/tasks/%d";

  public static final String TASKS_EXECUTOR_URI = "/tasks-executor/api/v1/tasks";
  public static final String TASKS_EXECUTOR_ID_URI = "/tasks-executor/api/v1/tasks/%d";

  public static final String USER = "user";
  public static final String PASSWORD = "password";
}
