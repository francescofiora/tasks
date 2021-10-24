package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Message Dto Request Impl.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public final class MessageDtoRequestImpl extends AbstractMessageDto
    implements MessageDtoRequest, Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  private Map<String, String> parameters = new HashMap<>();

  /**
   * builder helper for parameters.
   *
   * @param parameters Map
   * @return MessageDtoRequestImpl
   */
  public MessageDtoRequestImpl addParameters(Map<String, String> parameters) {
    if (parameters != null && !parameters.isEmpty()) {
      this.parameters.putAll(parameters);
    }
    return this;
  }

  public MessageDtoRequestImpl addParameter(String key, String value) {
    parameters.put(key, value);
    return this;
  }

  public MessageDtoRequestImpl taskId(Long taskId) {
    setTaskId(taskId);
    return this;
  }

  public MessageDtoRequestImpl type(TaskType type) {
    setType(type);
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
    return Objects.equals(getTaskId(), ((MessageDtoRequestImpl) obj).getTaskId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getTaskId());
  }
}
