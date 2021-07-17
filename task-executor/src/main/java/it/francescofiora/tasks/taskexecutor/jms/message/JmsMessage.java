package it.francescofiora.tasks.taskexecutor.jms.message;

import it.francescofiora.tasks.message.MessageDtoRequest;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class JmsMessage {

  private final MessageDtoRequest request;
  private final String jmsMessageId;
  private final Long timestamp;

  @Override
  public int hashCode() {
    return Objects.hashCode(getJmsMessageId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getJmsMessageId(), ((JmsMessage) obj).getJmsMessageId());
  }

  @Override
  public String toString() {
    return "JmsMessage {request=" + getRequest() + ", jmsMessageId=" + getJmsMessageId()
        + ", timestamp=" + getTimestamp() + "}";
  }
}
