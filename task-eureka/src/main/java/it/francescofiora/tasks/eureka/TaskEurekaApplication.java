package it.francescofiora.tasks.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Application Starter.
 */
@EnableEurekaServer
@SpringBootApplication
public class TaskEurekaApplication {

  public static void main(String[] args) {
    SpringApplication.run(TaskEurekaApplication.class, args);
  }
}
