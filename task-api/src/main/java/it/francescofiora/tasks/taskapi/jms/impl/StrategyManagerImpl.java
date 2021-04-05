package it.francescofiora.tasks.taskapi.jms.impl;

import it.francescofiora.tasks.taskapi.jms.StrategyManager;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import it.francescofiora.tasks.taskapi.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StrategyManagerImpl implements StrategyManager {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final TaskService taskService;

  public StrategyManagerImpl(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  public void exec(JmsMessage message) {
    log.info("Response - message: " + message.getJmsMessageId() + "; task: "
        + message.getResponse().getTaskId());
    taskService.response(message.getResponse());
  }

}
