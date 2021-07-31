package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;

/**
 * Jms Validator.
 */
public interface JmsValidator {

  JmsMessage validate(Object obj);
}
