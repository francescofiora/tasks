package it.francescofiora.tasks.taskapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParameterDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Schema(description = "name", example = "name", required = true)
  @JsonProperty("name")
  private String name;

  @NotBlank
  @Schema(description = "value", example = "value", required = true)
  @JsonProperty("value")
  private String value;

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
    if ((other.getName() == null && other.getValue() == null)
        || (getName() == null && getValue() == null)) {
      return false;
    }
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getValue(), other.getValue());
  }

  @Override
  public String toString() {
    return "ParameterDto{" + "name='" + getName() + "'" + ", value='" + getValue() + "'" + "}";
  }
}
