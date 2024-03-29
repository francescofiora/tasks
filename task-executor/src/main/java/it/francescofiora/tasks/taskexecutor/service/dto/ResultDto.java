package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Result Dto.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class ResultDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Schema(description = "Value", example = "value", required = true)
  @JsonProperty("value")
  private String value;

  public ResultDto() {}

  public ResultDto(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getValue(), ((ResultDto) obj).getValue());
  }
}
