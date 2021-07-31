package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;

/**
 * Jms Validator.
 */
public interface JmsValidator {

  JmsMessage validate(Object obj);
}
