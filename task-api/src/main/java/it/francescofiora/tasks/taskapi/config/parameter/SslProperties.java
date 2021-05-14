package it.francescofiora.tasks.taskapi.config.parameter;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

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
