package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.util.DtoIdentifier;
import it.francescofiora.tasks.util.DtoUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefTaskDto implements Serializable, DtoIdentifier {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Schema(description = "Unique Task identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @NotNull
  @Schema(description = "type of task", example = "SHORT", required = true)
  @JsonProperty("type")
  private TaskType type;

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public String toString() {
    return "RefTaskDto {id=" + getId() + ", type=" + getType() + "}";
  }
}
