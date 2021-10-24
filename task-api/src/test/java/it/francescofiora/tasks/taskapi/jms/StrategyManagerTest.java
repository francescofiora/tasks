package it.francescofiora.tasks.taskapi.jms;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.jms.impl.StrategyManagerImpl;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import it.francescofiora.tasks.taskapi.service.TaskService;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class StrategyManagerTest {

  private StrategyManager strategyManager;

  private TaskService spyTaskService;

  /**
   * Set up.
   */
  @BeforeEach
  void setUp() {
    spyTaskService = spy(mock(TaskService.class));
    strategyManager = new StrategyManagerImpl(spyTaskService);
  }

  @Test
  void testExec() {
    var response = new MessageDtoResponseImpl().taskId(1L);
    var message = new JmsMessage(response, "ID", new Date().getTime());
    strategyManager.exec(message);
    verify(spyTaskService).response(eq(response));
  }

}
