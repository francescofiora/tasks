package it.francescofiora.tasks.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("it.francescofiora.tasks.taskapi.config.parameter")
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
