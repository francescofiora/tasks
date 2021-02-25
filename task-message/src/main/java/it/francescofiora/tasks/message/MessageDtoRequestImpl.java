package it.francescofiora.tasks.message;

import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public final class MessageDtoRequestImpl extends MessageDtoImpl
    implements MessageDtoRequest, Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  private Map<String, String> parameters = new HashMap<>();

  /**
   * builder helper for parameters.
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
  public String toString() {
    return "MessageDtoRequestImpl [parameters=" + getParameters() + ", type=" + getType()
        + ", taskId=" + getTaskId() + "]";
  }
}
