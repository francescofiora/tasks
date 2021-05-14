package it.francescofiora.tasks.taskexecutor.jms.message;

import it.francescofiora.tasks.message.MessageDtoRequest;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class JmsMessage {

  private final MessageDtoRequest request;
  private final String jmsMessageId;
  private final Long timestamp;

  /**
   * Constructor.
   *
   * @param request MessageDtoRequest
   * @param jmsMessageId String
   * @param timestamp long
   */
  public JmsMessage(MessageDtoRequest request, String jmsMessageId, Long timestamp) {
    this.request = request;
    this.jmsMessageId = jmsMessageId;
    this.timestamp = timestamp;
  }

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
