package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewTaskDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Schema(description = "description", example = "description", required = true)
  @JsonProperty("description")
  private String description;

  @NotNull
  @Schema(description = "type of task", example = "SHORT", required = true)
  @JsonProperty("type")
  private TaskType type;

  @NotEmpty
  @Valid
  @Schema(required = true)
  @JsonProperty("parameters")
  private Set<ParameterDto> parameters = new HashSet<>();

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    NewTaskDto other = (NewTaskDto) obj;
    if (description == null) {
      if (other.description != null) {
        return false;
      }
    } else {
      if (!description.equals(other.description)) {
        return false;
      }
    }
    if (parameters == null) {
      if (other.parameters != null) {
        return false;
      }
    } else {
      if (!parameters.equals(other.parameters)) {
        return false;
      }
    }
    if (type != other.type) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "TaskDto{description='" + getDescription() + "', type='" + getType() + "'}";
  }
}
