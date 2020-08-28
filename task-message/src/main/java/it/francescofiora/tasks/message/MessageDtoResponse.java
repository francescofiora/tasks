package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskStatus;

import javax.validation.constraints.NotNull;

public interface MessageDtoResponse extends MessageDto {

  @NotNull
  TaskStatus getStatus();
  
  @NotNull
  String getResult();
}
