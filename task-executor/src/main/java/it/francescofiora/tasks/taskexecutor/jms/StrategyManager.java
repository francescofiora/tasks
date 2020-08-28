package it.francescofiora.tasks.taskexecutor.jms;

public interface StrategyManager {

  void exec(JmsEvent event);
}
