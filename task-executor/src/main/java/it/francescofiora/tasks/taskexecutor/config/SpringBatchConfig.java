package it.francescofiora.tasks.taskexecutor.config;

import it.francescofiora.tasks.taskexecutor.tasklet.SaveDbTasklet;
import it.francescofiora.tasks.taskexecutor.tasklet.SendMsgTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

  @Bean("saveDbStep")
  public Step saveDbStep(StepBuilderFactory stepBuilderFactory, SaveDbTasklet saveDbTasklet) {
    return stepBuilderFactory.get(SaveDbTasklet.NAME).allowStartIfComplete(true)
        .tasklet(saveDbTasklet).build();
  }

  @Bean("sendMsgStep")
  public Step sendMsgStep(StepBuilderFactory stepBuilderFactory, SendMsgTasklet sendMsgTasklet) {
    return stepBuilderFactory.get(SendMsgTasklet.NAME).allowStartIfComplete(true)
        .tasklet(sendMsgTasklet).build();
  }
}
