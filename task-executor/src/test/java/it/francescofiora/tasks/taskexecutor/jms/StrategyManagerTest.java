package it.francescofiora.tasks.taskexecutor.jms;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.jms.errors.JmsException;
import it.francescofiora.tasks.taskexecutor.jms.impl.StrategyManagerImpl;
import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;
import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class StrategyManagerTest {

  private StrategyManager strategyManager;

  private JobLauncher jobLauncher;

  private Job jobLong;
  private Job jobShort;
  private Job jobNope;

  /**
   * Set up.
   */
  @BeforeEach
  void setUp() {
    jobLauncher = spy(mock(JobLauncher.class));

    jobLong = mock(Job.class);
    when(jobLong.getName()).thenReturn(JobType.LONG.name());

    jobShort = mock(Job.class);
    when(jobShort.getName()).thenReturn(JobType.SHORT.name());

    jobNope = mock(Job.class);
    when(jobNope.getName()).thenReturn(JobType.NOPE.name());

    strategyManager = new StrategyManagerImpl(jobLauncher, new Job[] {jobLong, jobShort, jobNope});
  }

  @Test
  void testExec() throws Exception {
    MessageDtoRequest request = TestUtils.createMessageDtoRequest();
    JmsMessage message = new JmsMessage(request, "ID", new Date().getTime());

    strategyManager.exec(message);
    verify(jobLauncher).run(eq(jobLong), any(JobParameters.class));
  }

  @Test
  void testExecNope() throws Exception {
    MessageDtoRequest request = TestUtils.createMessageDtoRequestNewType();
    JmsMessage message = new JmsMessage(request, "ID", new Date().getTime());

    strategyManager.exec(message);
    verify(jobLauncher).run(eq(jobNope), any(JobParameters.class));
  }

  @Test
  void testExecException() throws Exception {
    when(jobLauncher.run(eq(jobLong), any(JobParameters.class))).thenThrow(new RuntimeException());

    MessageDtoRequest request = TestUtils.createMessageDtoRequest();
    JmsMessage message = new JmsMessage(request, "ID", new Date().getTime());

    assertThrows(JmsException.class, () -> strategyManager.exec(message));
  }
}
