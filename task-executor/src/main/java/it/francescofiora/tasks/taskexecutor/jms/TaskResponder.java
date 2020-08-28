package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.message.MessageDtoResponse;

public interface TaskResponder {

  void send(MessageDtoResponse response);
}
