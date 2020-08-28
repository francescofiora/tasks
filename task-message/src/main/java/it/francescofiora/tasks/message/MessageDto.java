package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskType;

import javax.validation.constraints.NotNull;

public interface MessageDto {

  @NotNull
  TaskType getType();

  @NotNull
  Long getTaskId();
}
