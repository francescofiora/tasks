package it.francescofiora.tasks.taskexecutor.tasklet.job;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {NopeJobTest.BatchConfiguration.class})
public class NopeJobTest {

  private static final Long ID = 1L;

  private static final Long TASK_REF = 10L;

  private static final Long TASK_REF_IN_PROGRESS = 20L;

  private static final Long MESSAGE_CREATED = 123456L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  void test() throws Exception {
    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));
    assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
  }

  @Test
  public void testTaskInProgress() throws Exception {
    Map<String, JobParameter> parameters = new HashMap<>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF_IN_PROGRESS));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));

    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));
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
      TaskService taskService = Mockito.mock(TaskService.class);
      Task task = new Task();
      task.setId(ID);
      Mockito.when(taskService.save(Mockito.any(Task.class))).thenReturn(task);

      Task taskInProgress = new Task();
      taskInProgress.setId(ID);
      taskInProgress.setStatus(TaskStatus.IN_PROGRESS);

      Mockito.when(taskService.findByTaskRef(Mockito.eq(TASK_REF_IN_PROGRESS)))
          .thenReturn(Optional.of(taskInProgress));

      return taskService;
    }

    @Bean
    public NopeTasklet getNopeTasklet(TaskService taskService) {
      return new NopeTasklet(taskService);
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils(Job nopeJob) throws NoSuchJobException {
      JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(nopeJob);

      return jobLauncherTestUtils;
    }
  }
}
