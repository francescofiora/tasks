package it.francescofiora.tasks.taskexecutor.config.parameter;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Ssl Properties.
 */
@Getter
@Setter
public class SslProperties {

  @NotBlank
  private String trustStorePath;

  @NotBlank
  private String trustStorePass;

  @NotBlank
  private String keyStorePath;

  @NotBlank
  private String keyStorePass;  
}
