package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * Message Dto Response Impl.
 */
@Getter
@Setter
public final class MessageDtoResponseImpl extends AbstractMessageDto
    implements MessageDtoResponse, Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  private TaskStatus status;

  @NotNull
  private String result;

  public MessageDtoResponseImpl taskId(Long taskId) {
    setTaskId(taskId);
    return this;
  }

  public MessageDtoResponseImpl type(TaskType type) {
    setType(type);
    return this;
  }

  public MessageDtoResponseImpl status(TaskStatus status) {
    this.status = status;
    return this;
  }

  public MessageDtoResponseImpl result(String result) {
    this.result = result;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getTaskId(), ((MessageDtoResponseImpl) obj).getTaskId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getTaskId());
  }

  @Override
  public String toString() {
    return "MessageDtoResponseImpl {status=" + getStatus() + ", result=" + getResult() + ", type="
        + getType() + ", taskId=" + getTaskId() + "}";
  }
}
