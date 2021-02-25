package it.francescofiora.tasks.taskexecutor.jms;

import lombok.Getter;
import it.francescofiora.tasks.message.MessageDtoRequest;

@Getter
public final class JmsEvent {

  private final MessageDtoRequest request;
  private final String jmsMessageId;
  private final Long timestamp;

  /**
   * Constructor.
   * 
   * @param request      MessageDtoRequest
   * @param jmsMessageId String
   * @param timestamp    long
   */
  public JmsEvent(MessageDtoRequest request, String jmsMessageId, Long timestamp) {
    this.request = request;
    this.jmsMessageId = jmsMessageId;
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "JmsEvent {request=" + getRequest() + ", jmsMessageId=" + getJmsMessageId()
        + ", timestamp=" + getTimestamp() + "}";
  }
}
