package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.message.MessageDtoRequest;

public interface TaskExecutor {

  void send(MessageDtoRequest request);
}
