package it.francescofiora.tasks.taskexecutor.jms;

import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;

/**
 * Strategy Manager.
 */
public interface StrategyManager {

  void exec(JmsMessage message);
}
