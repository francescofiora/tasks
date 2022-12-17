package it.francescofiora.tasks.taskapi.jms;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.jms.impl.StrategyManagerImpl;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import it.francescofiora.tasks.taskapi.service.TaskService;
import java.util.Date;
import org.junit.jupiter.api.Test;

class StrategyManagerTest {

  @Test
  void testExec() {
    var response = new MessageDtoResponseImpl().taskId(1L);
    var message = new JmsMessage(response, "ID", new Date().getTime());
    var taskService = mock(TaskService.class);
    var strategyManager = new StrategyManagerImpl(taskService);
    strategyManager.exec(message);
    verify(taskService).response(response);
  }
}
