package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.message.MessageDtoRequest;

public interface JmsProducer {

  void send(MessageDtoRequest request);
}
