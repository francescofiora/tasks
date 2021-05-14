package it.francescofiora.tasks.taskexecutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("it.francescofiora.tasks.taskexecutor.config.parameter")
public class Application {

  /**
   * Main method.
   *
   * @param args args
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
