package it.francescofiora.tasks.taskexecutor.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import it.francescofiora.tasks.taskexecutor.config.SpringBatchConfig;
import it.francescofiora.tasks.taskexecutor.config.job.ShortJobConfig;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.HashMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
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

@ContextConfiguration(classes = {ShortTaskletTest.BatchConfiguration.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class ShortTaskletTest {

  private static final Long ID = 1L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private TaskService taskService;

  @Test
  void testShortTasklet() {
    var map = new HashMap<String, Object>();
    var task = new Task();
    task.setId(ID);
    map.put(AbstractTasklet.TASK, task);

    var jobExecution =
        jobLauncherTestUtils.launchStep(ShortTasklet.NAME, new ExecutionContext(map));
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    verify(taskService).save(task);
  }

  @Configuration
  @Import({SpringBatchConfig.class, ShortTasklet.class, ShortJobConfig.class})
  static class BatchConfiguration {

    @MockBean
    private TaskService taskService;

    @MockBean
    private SaveDbTasklet saveDbTasklet;

    @MockBean
    private SendMsgTasklet sendMsgTasklet;

    private Job createFakeJob(JobBuilderFactory jobBuilderFactory, Step step) {
      return jobBuilderFactory.get("fakeJob").incrementer(new RunIdIncrementer()).start(step)
          .build();
    }

    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils(JobBuilderFactory jobBuilderFactory,
        JobLauncher jobLauncher, JobRepository jobRepository, Step shortStep) {
      var jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(createFakeJob(jobBuilderFactory, shortStep));
      jobLauncherTestUtils.setJobRepository(jobRepository);
      jobLauncherTestUtils.setJobLauncher(jobLauncher);

      return jobLauncherTestUtils;
    }
  }
}
