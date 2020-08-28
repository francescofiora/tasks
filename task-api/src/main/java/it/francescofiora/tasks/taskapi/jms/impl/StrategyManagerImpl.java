package it.francescofiora.tasks.taskapi.jms.impl;

import it.francescofiora.tasks.taskapi.jms.JmsEvent;
import it.francescofiora.tasks.taskapi.jms.StrategyManager;
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
  public void exec(JmsEvent event) {
    log.info("Response - message: " + event.getJmsMessageId() + "; task: "
        + event.getResponse().getTaskId());
    taskService.response(event.getResponse());
  }

}
