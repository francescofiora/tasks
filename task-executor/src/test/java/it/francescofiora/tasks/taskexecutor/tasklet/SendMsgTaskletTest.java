package it.francescofiora.tasks.taskexecutor.tasklet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.config.SpringBatchConfig;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.jms.JmsProducer;
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

@ContextConfiguration(classes = {SendMsgTaskletTest.BatchConfiguration.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class SendMsgTaskletTest {

  private static final Long TASK_REF = 10L;

  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private JmsProducer jmsProducer;

  private Task createTask() {
    return new Task().taskRef(TASK_REF).status(TaskStatus.IN_PROGRESS)
        .taskType(TaskType.NEW_TYPE.name());
  }

  @Test
  void testSaveDbTasklet() {
    var map = new HashMap<String, Object>();
    map.put(SendMsgTasklet.TASK, createTask());
    var jobExecutionContext = new ExecutionContext(map);

    var jobExecution = jobLauncherTestUtils.launchStep(SendMsgTasklet.NAME, jobExecutionContext);
    assertThat(jobExecution).isNotNull();
    assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    verify(jmsProducer).send(any(MessageDtoResponse.class));
  }

  @Configuration
  @Import({SpringBatchConfig.class, SendMsgTasklet.class})
  static class BatchConfiguration {

    @MockBean
    private JmsProducer jmsProducer;

    @MockBean
    private SaveDbTasklet saveDbTasklet;

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, Step sendMsgStep) {
      return jobBuilderFactory.get("fakeJob").incrementer(new RunIdIncrementer()).start(sendMsgStep)
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
