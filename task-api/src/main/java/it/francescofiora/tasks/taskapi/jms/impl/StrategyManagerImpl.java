package it.francescofiora.tasks.taskapi.jms.impl;

import it.francescofiora.tasks.taskapi.jms.StrategyManager;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import it.francescofiora.tasks.taskapi.service.TaskService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Strategy Manager Impl.
 */
@Component
@AllArgsConstructor
public class StrategyManagerImpl implements StrategyManager {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final TaskService taskService;

  @Override
  public void exec(JmsMessage message) {
    log.info("Response - message: " + message.getJmsMessageId() + "; task: "
        + message.getResponse().getTaskId());
    taskService.response(message.getResponse());
  }

}
