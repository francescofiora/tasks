package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.message.MessageDtoResponse;

public interface JmsProducer {

  void send(MessageDtoResponse response);
}
