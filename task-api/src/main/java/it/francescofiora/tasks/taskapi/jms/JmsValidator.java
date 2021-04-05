package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;

public interface JmsValidator {

  JmsMessage validate(Object obj);
}
