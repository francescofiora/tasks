package it.francescofiora.tasks.taskapi.jms;

public interface StrategyManager {

  void exec(JmsEvent event);
}
