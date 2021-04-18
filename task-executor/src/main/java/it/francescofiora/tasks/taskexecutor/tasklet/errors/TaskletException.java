package it.francescofiora.tasks.taskexecutor.tasklet.errors;

public class TaskletException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TaskletException(String message) {
    super(message);
  }
}
