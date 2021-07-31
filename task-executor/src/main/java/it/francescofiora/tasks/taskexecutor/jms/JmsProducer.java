package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.message.MessageDtoResponse;

/**
 * Jms Producer.
 */
public interface JmsProducer {

  void send(MessageDtoResponse response);
}
