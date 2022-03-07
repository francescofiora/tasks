package it.francescofiora.tasks.taskapi.config.parameter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Jms Properties.
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "spring.activemq")
public class JmsProperties {

  @NotBlank
  private String brokerUrl;

  @NotBlank
  private String user;

  @Valid
  @NotNull  
  private SslProperties ssl;
}
