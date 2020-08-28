package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

@Validated
public class ParameterDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "name", example = "name", required = true)
  @JsonProperty("name")
  private String name;

  @Schema(description = "value", example = "value", required = true)
  @JsonProperty("value")
  private String value;

  @NotBlank
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @NotBlank
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ParameterDto other = (ParameterDto) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else {
      if (!name.equals(other.name)) {
        return false;
      }
    }
    if (value == null) {
      if (other.value != null) {
        return false;
      }
    } else {
      if (!value.equals(other.value)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return "ParameterDto{" + "name='" + getName() + "'" + ", value='" + getValue() + "'" + "}";
  }
}
