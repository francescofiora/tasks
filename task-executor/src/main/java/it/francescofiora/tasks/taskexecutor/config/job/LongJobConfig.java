package it.francescofiora.tasks.taskexecutor.config.job;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.tasklet.LongTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LongJobConfig {

  @Bean("longJob")
  public Job longJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step longStep) {
    return jobBuilderFactory.get(JobType.LONG.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(longStep).next(sendMsgStep).build();
  }

  @Bean("longStep")
  public Step longStep(StepBuilderFactory stepBuilderFactory, LongTasklet longTasklet) {
    return stepBuilderFactory.get(LongTasklet.NAME).allowStartIfComplete(true).tasklet(longTasklet)
        .build();
  }
}
