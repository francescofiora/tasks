package it.francescofiora.tasks.taskapi.jms.message;

import it.francescofiora.tasks.message.MessageDtoResponse;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class JmsMessage {

  private final MessageDtoResponse response;
  private final String jmsMessageId;
  private final Long timestamp;

  /**
   * Constructor.
   * 
   * @param response     MessageDtoResponse
   * @param jmsMessageId String
   * @param timestamp    long
   */
  public JmsMessage(MessageDtoResponse response, String jmsMessageId, Long timestamp) {
    this.response = response;
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
    return "JmsMessage {response=" + getResponse() + ", jmsMessageId=" + getJmsMessageId()
        + ", timestamp=" + getTimestamp() + "}";
  }
}
