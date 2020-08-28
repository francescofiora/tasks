package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskType;

import javax.validation.constraints.NotNull;

public abstract class MessageDtoImpl implements MessageDto {

  private Long taskId;
  private TaskType type;
  
  @Override
  public @NotNull TaskType getType() {
    return type;
  }

  @Override
  public @NotNull Long getTaskId() {
    return taskId;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public void setType(TaskType type) {
    this.type = type;
  }
}
