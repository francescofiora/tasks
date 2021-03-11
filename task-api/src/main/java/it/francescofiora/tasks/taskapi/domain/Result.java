package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class Result implements Serializable {

  private static final long serialVersionUID = 1L;

  @Field("value")
  private String value;

  public Result value(String value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Result)) {
      return false;
    }
    return value != null && value.equals(((Result) o).value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }

  @Override
  public String toString() {
    return "Result {value=" + value + "}";
  }
}
