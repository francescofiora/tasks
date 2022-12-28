package it.francescofiora.tasks.taskexecutor.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.config.SpringBatchConfig;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SaveDbTaskletTest.BatchConfiguration.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class SaveDbTaskletTest {

  private static final Long ID = 1L;

  private static final Long TASK_REF = 10L;

  private static final Long TASK_REF_IN_PROGRESS = 20L;

  private static final Long MESSAGE_CREATED = 123456L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Test
  void testSaveDbTasklet() {
    var parameters = new HashMap<String, JobParameter>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));
    var jobParameters = new JobParameters(parameters);

    var jobExecution =
        jobLauncherTestUtils.launchStep(SaveDbTasklet.NAME, jobParameters, new ExecutionContext());
    assertThat(jobExecution).isNotNull();
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    assertThat(jobExecution.getExecutionContext().containsKey(SaveDbTasklet.TASK));
    var task = (Task) jobExecution.getExecutionContext().get(SaveDbTasklet.TASK);
    assertThat(task).isNotNull();
    assertThat(task.getId()).isEqualTo(ID);
  }

  @Test
  void testTaskInProgress() {
    var parameters = new HashMap<String, JobParameter>();
    parameters.put(JmsParameters.TASK_REF, new JobParameter(TASK_REF_IN_PROGRESS));
    parameters.put(JmsParameters.MESSAGE_CREATED, new JobParameter(MESSAGE_CREATED));
    parameters.put(JmsParameters.JOB_TYPE, new JobParameter(JobType.NOPE.name()));

    var jobExecution = jobLauncherTestUtils.launchStep(SaveDbTasklet.NAME,
        new JobParameters(parameters), new ExecutionContext());
    assertThat(jobExecution).isNotNull();
    assertThat(jobExecution.getExitStatus().getExitCode())
        .isEqualTo(ExitStatus.FAILED.getExitCode());
  }

  @Configuration
  @Import({SpringBatchConfig.class, SaveDbTasklet.class})
  static class BatchConfiguration {

    @Bean
    public TaskService taskService() {
      var taskService = mock(TaskService.class);

      var task = new Task();
      task.setId(ID);
      when(taskService.save(any(Task.class))).thenReturn(task);

      var taskInProgress = new Task();
      taskInProgress.setId(ID);
      taskInProgress.setStatus(TaskStatus.IN_PROGRESS);

      when(taskService.findByTaskRef(TASK_REF_IN_PROGRESS))
          .thenReturn(Optional.of(taskInProgress));

      return taskService;
    }

    @MockBean
    private SendMsgTasklet sendMsgTasklet;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step saveDbStep) {
      return jobBuilderFactory.get("fakeJob").incrementer(new RunIdIncrementer()).start(saveDbStep)
          .build();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils(JobLauncher jobLauncher,
        JobRepository jobRepository, Job job) {
      var jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(job);
      jobLauncherTestUtils.setJobRepository(jobRepository);
      jobLauncherTestUtils.setJobLauncher(jobLauncher);

      return jobLauncherTestUtils;
    }
  }
}
