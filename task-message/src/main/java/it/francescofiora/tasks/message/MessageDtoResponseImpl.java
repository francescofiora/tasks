package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public final class MessageDtoResponseImpl extends MessageDtoImpl
    implements MessageDtoResponse, Serializable {

  private static final long serialVersionUID = 1L;

  private TaskStatus status;
  private String result;

  public MessageDtoResponseImpl taskId(Long taskId) {
    setTaskId(taskId);
    return this;
  }

  public MessageDtoResponseImpl type(TaskType type) {
    setType(type);
    return this;
  }

  @Override
  public @NotNull TaskStatus getStatus() {
    return status;
  }

  public MessageDtoResponseImpl status(TaskStatus status) {
    this.status = status;
    return this;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  @Override
  public @NotNull String getResult() {
    return result;
  }

  public MessageDtoResponseImpl result(String result) {
    this.result = result;
    return this;
  }
  
  public void setResult(String result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "MessageDtoResponseImpl {status=" + getStatus() + ", result=" + getResult() + ", type="
        + getType() + ", taskId=" + getTaskId() + "}";
  }
}
