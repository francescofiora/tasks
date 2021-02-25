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
public class ResultDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotBlank
  @Schema(description = "value", example = "value", required = true)
  @JsonProperty("value")
  private String value;

  public ResultDto() {
  }
  
  public ResultDto(String value) {
    super();
    this.value = value;
  }

  public ResultDto value(String value) {
    this.value = value;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    ResultDto other = (ResultDto) obj;
    if (other.getValue() == null || getValue() == null) {
      return false;
    }
    return Objects.equals(getValue(), other.getValue());
  }

  @Override
  public String toString() {
    return "ResultDto {value=" + value + "}";
  }
}
