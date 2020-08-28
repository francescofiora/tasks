package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskType;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class RefTaskDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Task identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @Schema(description = "type of task", example = "SHORT", required = true)
  @JsonProperty("type")
  private TaskType type;

  public RefTaskDto() {

  }

  /**
   * constructor.
   * 
   * @param id   Long
   * @param type TaskType
   */
  public RefTaskDto(Long id, TaskType type) {
    super();
    this.id = id;
    this.type = type;
  }

  @NotNull
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  public TaskType getType() {
    return type;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RefTaskDto other = (RefTaskDto) o;
    if (other.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), other.getId());
  }

  @Override
  public String toString() {
    return "RefTaskDto {id=" + getId() + ", type=" + getType() + "}";
  }

}
