package it.francescofiora.tasks.taskexecutor.service.dto;

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
  @Schema(description = "Name", example = "name", required = true)
  @JsonProperty("name")
  private String name;

  @NotBlank
  @Schema(description = "Value", example = "value", required = true)
  @JsonProperty("value")
  private String value;

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ParameterDto other = (ParameterDto) obj;
    return Objects.equals(getName(), other.getName())
        && Objects.equals(getValue(), other.getValue());
  }

  @Override
  public String toString() {
    return "ParameterDto{" + "name='" + getName() + "'" + ", value='" + getValue() + "'" + "}";
  }
}
