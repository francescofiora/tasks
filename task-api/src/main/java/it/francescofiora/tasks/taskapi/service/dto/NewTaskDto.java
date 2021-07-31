package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * New Task Dto.
 */
@Getter
@Setter
public class NewTaskDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Schema(description = "Description", example = "description", required = true)
  @JsonProperty("description")
  private String description;

  @NotNull
  @Schema(description = "Type of task", example = "SHORT", required = true)
  @JsonProperty("type")
  private TaskType type;

  @NotEmpty
  @Valid
  @Schema(required = true)
  @JsonProperty("parameters")
  private Set<ParameterDto> parameters = new HashSet<>();

  @Override
  public int hashCode() {
    return Objects.hash(getDescription(), getParameters(), getType());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    var other = (NewTaskDto) obj;
    return Objects.equals(getDescription(), other.getDescription())
        && Objects.equals(getParameters(), other.getParameters())
        && Objects.equals(getType(), other.getType());
  }

  @Override
  public String toString() {
    return "TaskDto{description='" + getDescription() + "', type='" + getType() + "'}";
  }
}
