package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.message.MessageDtoRequest;

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

  public MessageDtoRequest getRequest() {
    return request;
  }

  public String getJmsMessageId() {
    return jmsMessageId;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "JmsEvent {request=" + getRequest() + ", jmsMessageId=" + getJmsMessageId()
        + ", timestamp=" + getTimestamp() + "}";
  }
}
