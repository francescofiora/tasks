package it.francescofiora.tasks.message;

import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 * Interface for Message Dto Request.
 */
public interface MessageDtoRequest extends MessageDto {

  @NotNull
  Map<String, String> getParameters();
}
