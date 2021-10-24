package it.francescofiora.tasks.taskapi.jms.message;

import it.francescofiora.tasks.message.MessageDtoResponse;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Jms Message.
 */
@Getter
@AllArgsConstructor
@ToString(callSuper = true, includeFieldNames = true)
public final class JmsMessage {

  private final MessageDtoResponse response;
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
}
