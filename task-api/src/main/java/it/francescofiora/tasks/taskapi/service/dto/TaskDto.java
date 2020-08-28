package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskStatus;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class TaskDto extends NewTaskDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @Schema(description = "status of task", example = "TERMINATED", required = true)
  @JsonProperty("status")
  private TaskStatus status;

  @Schema(description = "result of execution", required = false)
  @JsonProperty("result")
  @Valid
  private ResultDto result;
  
  @NotNull
  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public ResultDto getResult() {
    return result;
  }

  public void setResult(ResultDto result) {
    this.result = result;
  }

  @NotBlank
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TaskDto taskDto = (TaskDto) o;
    if (taskDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), taskDto.getId());
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
