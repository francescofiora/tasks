package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

@Validated
public class UpdatableTaskDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;
  
  @Schema(description = "description", example = "description", required = true)
  @JsonProperty("description")
  private String description;

  @NotBlank
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotBlank
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    UpdatableTaskDto taskDto = (UpdatableTaskDto) o;
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
    return "TaskDto{" + "id='" + getId() + "', description='" + getDescription() + "'}";
  }
}
