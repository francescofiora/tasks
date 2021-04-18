package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;

public interface JmsValidator {

  JmsMessage validate(Object obj);
}
