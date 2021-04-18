package it.francescofiora.tasks.taskexecutor.tasklet;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.config.SpringBatchConfig;
import it.francescofiora.tasks.taskexecutor.config.job.LongJobConfig;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LongTaskletTest.BatchConfiguration.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class LongTaskletTest {

  private static final Long ID = 1L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @SpyBean
  private TaskService taskService;

  @Test
  void testLongTasklet() throws Exception {
    Map<String, Object> map = new HashMap<>();
    Task task = new Task();
    task.setId(ID);
    map.put(AbstractTasklet.TASK, task);

    JobExecution jobExecution =
        jobLauncherTestUtils.launchStep(LongTasklet.NAME, new ExecutionContext(map));
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    Mockito.verify(taskService).save(Mockito.eq(task));
  }

  @Configuration
  @Import({SpringBatchConfig.class, LongTasklet.class, LongJobConfig.class})
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
        JobLauncher jobLauncher, JobRepository jobRepository, Step longStep) {
      JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
      jobLauncherTestUtils.setJob(createFakeJob(jobBuilderFactory, longStep));
      jobLauncherTestUtils.setJobRepository(jobRepository);
      jobLauncherTestUtils.setJobLauncher(jobLauncher);

      return jobLauncherTestUtils;
    }
  }
}
