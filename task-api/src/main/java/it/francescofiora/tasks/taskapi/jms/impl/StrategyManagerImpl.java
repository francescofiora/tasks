package it.francescofiora.tasks.taskapi.jms.impl;

import it.francescofiora.tasks.taskapi.jms.StrategyManager;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import it.francescofiora.tasks.taskapi.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Strategy Manager Impl.
 */
@Slf4j
@Component
@AllArgsConstructor
public class StrategyManagerImpl implements StrategyManager {

  private final TaskService taskService;

  @Override
  public void exec(JmsMessage message) {
    log.info("Response - message: " + message.getJmsMessageId() + "; task: "
        + message.getResponse().getTaskId());
    taskService.response(message.getResponse());
  }

}
