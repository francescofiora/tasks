package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.message.MessageDtoResponse;

public final class JmsEvent {

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
  public JmsEvent(MessageDtoResponse response, String jmsMessageId, Long timestamp) {
    this.response = response;
    this.jmsMessageId = jmsMessageId;
    this.timestamp = timestamp;
  }

  public MessageDtoResponse getResponse() {
    return response;
  }

  public String getJmsMessageId() {
    return jmsMessageId;
  }

  public Long getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "JmsEvent {response=" + getResponse() + ", jmsMessageId=" + getJmsMessageId()
        + ", timestamp=" + getTimestamp() + "}";
  }
}
