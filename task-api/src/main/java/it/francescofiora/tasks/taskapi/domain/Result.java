package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Result Document.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class Result implements Serializable {

  private static final long serialVersionUID = 1L;

  @Field("value")
  private String value;

  public Result() {
  }

  public Result(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (getValue() == null || obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getValue(), ((Result) obj).value);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
