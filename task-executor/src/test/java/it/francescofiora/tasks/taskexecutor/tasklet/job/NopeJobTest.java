package it.francescofiora.tasks.taskexecutor.tasklet.job;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.config.SpringBatchConfig;
import it.francescofiora.tasks.taskexecutor.config.job.NopeJobConfig;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.tasklet.JmsParameters;
import it.francescofiora.tasks.taskexecutor.tasklet.NopeTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.SaveDbTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.SendMsgTasklet;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NopeJobTest.BatchConfiguration.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class NopeJobTest {

  private static final Long ID = 1L;

  private static final Long TASK_REF = 10L;

  private static final Long TASK_REF_IN_PROGRESS = 20L;

  private static final Long MESSAGE_CREATED = 123456L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  void test() throws Exception {
    var parameters = new HashMap<String, JobParameter>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));
    var jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
  }

  @Test
  void testTaskInProgress() throws Exception {
    var parameters = new HashMap<String, JobParameter>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF_IN_PROGRESS));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));

    var jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));
    assertThat(jobExecution).isNotNull();
    assertThat(jobExecution.getExitStatus().getExitCode())
        .isEqualTo(ExitStatus.FAILED.getExitCode());
  }

  @Configuration
  @Import({SpringBatchConfig.class, NopeJobConfig.class, SaveDbTasklet.class})
  static class BatchConfiguration {

    @MockBean
    private SendMsgTasklet sendMsgTasklet;

    @Bean
    public TaskService taskService() {
      var taskService = mock(TaskService.class);
      var task = new Task();
      task.setId(ID);
      when(taskService.save(any(Task.class))).thenReturn(task);

      var taskInProgress = new Task();
      taskInProgress.setId(ID);
      taskInProgress.setStatus(TaskStatus.IN_PROGRESS);

      when(taskService.findByTaskRef(TASK_REF_IN_PROGRESS)).thenReturn(Optional.of(taskInProgress));

      return taskService;
    }

    @Bean
    public NopeTasklet getNopeTasklet(TaskService taskService) {
      return new NopeTasklet(taskService);
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils(Job nopeJob) throws NoSuchJobException {
      var jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(nopeJob);

      return jobLauncherTestUtils;
    }
  }
}
