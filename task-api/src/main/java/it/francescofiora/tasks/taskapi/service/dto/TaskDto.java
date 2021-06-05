package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.util.DtoIdentifier;
import it.francescofiora.tasks.util.DtoUtils;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto extends NewTaskDto implements Serializable, DtoIdentifier {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @NotNull
  @Schema(description = "Status of task", example = "TERMINATED", required = true)
  @JsonProperty("status")
  private TaskStatus status;

  @NotNull
  @Valid
  @Schema(description = "Result of execution", required = false)
  @JsonProperty("result")
  private ResultDto result;
  
  @Override
  public boolean equals(Object obj) {
    return DtoUtils.equals(this, obj);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "TaskDto{id='" + getId() + "', description='" + getDescription() + "', type='"
        + getType() + "', status='" + getStatus() + "'}";
  }
}
