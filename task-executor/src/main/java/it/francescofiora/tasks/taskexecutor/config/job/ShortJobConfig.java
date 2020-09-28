package it.francescofiora.tasks.taskexecutor.config.job;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.tasklet.ShortTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortJobConfig {

  @Bean("shortJob")
  public Job shortJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step shortStep) {
    return jobBuilderFactory.get(JobType.SHORT.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(shortStep).next(sendMsgStep).build();
  }

  @Bean("shortStep")
  public Step shortStep(StepBuilderFactory stepBuilderFactory, ShortTasklet shortTasklet) {
    return stepBuilderFactory.get(ShortTasklet.NAME).allowStartIfComplete(true)
        .tasklet(shortTasklet).build();
  }
}
