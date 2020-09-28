package it.francescofiora.tasks.taskexecutor.config.job;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.tasklet.NopeTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NopeJobConfig {

  @Bean("nopeJob")
  public Job nopeJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step nopeStep) {
    return jobBuilderFactory.get(JobType.NOPE.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(nopeStep).next(sendMsgStep).build();
  }

  @Bean("nopeStep")
  public Step nopeStep(StepBuilderFactory stepBuilderFactory, NopeTasklet nopeTasklet) {
    return stepBuilderFactory.get(NopeTasklet.NAME).allowStartIfComplete(true).tasklet(nopeTasklet)
        .build();
  }
}
