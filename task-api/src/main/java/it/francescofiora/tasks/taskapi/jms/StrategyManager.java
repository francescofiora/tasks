package it.francescofiora.tasks.taskapi.jms;

import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;

/**
 * Strategy Manager.
 */
public interface StrategyManager {

  void exec(JmsMessage message);
}
