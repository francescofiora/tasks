package it.francescofiora.tasks.taskapi.config.parameter;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Profile("!dev")
@Validated
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class DbProperties {

  @NotBlank
  private String database;

  @NotBlank
  private String keystorefile;

  @NotBlank
  private String keystorepassword;

  @NotBlank
  private String truststorefile;

  @NotBlank
  private String truststorepassword;

  @NotBlank
  private String uri;
}
