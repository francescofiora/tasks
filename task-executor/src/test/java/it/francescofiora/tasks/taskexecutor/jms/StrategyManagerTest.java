package it.francescofiora.tasks.taskexecutor.jms;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.jms.errors.JmsException;
import it.francescofiora.tasks.taskexecutor.jms.impl.StrategyManagerImpl;
import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;
import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

class StrategyManagerTest {

  private static final String MESSAGE_ID = "ID";

  private Job createJob(JobType type) {
    var job = mock(Job.class);
    when(job.getName()).thenReturn(type.name());
    return job;
  }

  @Test
  void testExec() throws Exception {
    var request = TestUtils.createMessageDtoRequest();
    var message = new JmsMessage(request, MESSAGE_ID, new Date().getTime());

    var jobLauncher = mock(JobLauncher.class);
    var jobLong = createJob(JobType.LONG);
    var strategyManager = new StrategyManagerImpl(jobLauncher,
        new Job[] {jobLong, createJob(JobType.SHORT), createJob(JobType.NOPE)});
    strategyManager.exec(message);
    verify(jobLauncher).run(eq(jobLong), any(JobParameters.class));
  }

  @Test
  void testExecNope() throws Exception {
    var request = TestUtils.createMessageDtoRequestNewType();
    var message = new JmsMessage(request, MESSAGE_ID, new Date().getTime());

    var jobLauncher = mock(JobLauncher.class);
    var jobNope = createJob(JobType.NOPE);
    var strategyManager = new StrategyManagerImpl(jobLauncher,
        new Job[] {createJob(JobType.LONG), createJob(JobType.SHORT), jobNope});
    strategyManager.exec(message);
    verify(jobLauncher).run(eq(jobNope), any(JobParameters.class));
  }

  @Test
  void testExecException() throws Exception {
    var jobLauncher = mock(JobLauncher.class);
    var jobLong = createJob(JobType.LONG);
    when(jobLauncher.run(eq(jobLong), any(JobParameters.class))).thenThrow(new RuntimeException());

    var request = TestUtils.createMessageDtoRequest();
    var message = new JmsMessage(request, MESSAGE_ID, new Date().getTime());

    var strategyManager = new StrategyManagerImpl(jobLauncher,
        new Job[] {jobLong, createJob(JobType.SHORT), createJob(JobType.NOPE)});
    assertThrows(JmsException.class, () -> strategyManager.exec(message));
  }
}
