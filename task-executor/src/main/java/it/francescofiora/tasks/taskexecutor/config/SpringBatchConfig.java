package it.francescofiora.tasks.taskexecutor.config;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.tasklet.LongTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.NopeTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.SaveDbTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.SendMsgTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.ShortTasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

  @Bean("shortJob")
  public Job shortJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step shortStep) {
    return jobBuilderFactory.get(JobType.SHORT.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(shortStep).next(sendMsgStep).build();
  }

  @Bean("longJob")
  public Job longJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step longStep) {
    return jobBuilderFactory.get(JobType.LONG.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(longStep).next(sendMsgStep).build();
  }

  @Bean("nopeJob")
  public Job nopeJob(JobBuilderFactory jobBuilderFactory, Step saveDbStep, Step sendMsgStep,
      Step nopeStep) {
    return jobBuilderFactory.get(JobType.NOPE.name()).incrementer(new RunIdIncrementer())
        .start(saveDbStep).next(nopeStep).next(sendMsgStep).build();
  }

  @Bean("shortStep")
  public Step shortStep(StepBuilderFactory stepBuilderFactory, ShortTasklet shortTasklet) {
    return stepBuilderFactory.get("shortStep").allowStartIfComplete(true).tasklet(shortTasklet)
        .build();
  }

  @Bean("longStep")
  public Step longStep(StepBuilderFactory stepBuilderFactory, LongTasklet longTasklet) {
    return stepBuilderFactory.get("longTasklet").allowStartIfComplete(true).tasklet(longTasklet)
        .build();
  }

  @Bean("nopeStep")
  public Step nopeStep(StepBuilderFactory stepBuilderFactory, NopeTasklet nopeTasklet) {
    return stepBuilderFactory.get("nopeStep").allowStartIfComplete(true).tasklet(nopeTasklet)
        .build();
  }

  @Bean("saveDbStep")
  public Step saveDbStep(StepBuilderFactory stepBuilderFactory, SaveDbTasklet saveDbTasklet) {
    return stepBuilderFactory.get("saveDbStep").allowStartIfComplete(true).tasklet(saveDbTasklet)
        .build();
  }

  @Bean("sendMsgStep")
  public Step sendMsgStep(StepBuilderFactory stepBuilderFactory, SendMsgTasklet sendMsgTasklet) {
    return stepBuilderFactory.get("sendMsgStep").allowStartIfComplete(true).tasklet(sendMsgTasklet)
        .build();
  }
}
