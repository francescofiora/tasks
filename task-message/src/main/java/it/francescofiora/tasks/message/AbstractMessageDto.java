package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Abstract classes for all Dto Messages.
 */
@Getter
@Setter
public abstract class AbstractMessageDto implements MessageDto {

  @NotNull
  private Long taskId;

  @NotNull
  private TaskType type;
}
