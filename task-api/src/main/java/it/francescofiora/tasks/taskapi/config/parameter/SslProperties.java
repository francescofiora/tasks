package it.francescofiora.tasks.taskapi.config.parameter;

import lombok.Getter;
import lombok.Setter;

/**
 * Ssl Properties.
 */
@Getter
@Setter
public class SslProperties {

  private String trustStorePath;

  private String trustStorePass;

  private String keyStorePath;

  private String keyStorePass;  
}
