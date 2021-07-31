package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import javax.validation.constraints.NotNull;

/**
 * Interface for all Messages Dto Response.
 */
public interface MessageDtoResponse extends MessageDto {

  @NotNull
  TaskStatus getStatus();
  
  @NotNull
  String getResult();
}
