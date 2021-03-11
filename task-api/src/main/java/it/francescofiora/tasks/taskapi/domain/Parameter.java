package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
public class Parameter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Field("name")
  private String name;

  @Field("value")
  private String value;

  public Parameter name(String name) {
    this.name = name;
    return this;
  }

  public Parameter value(String value) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Parameter)) {
      return false;
    }
    return name != null && name.equals(((Parameter) o).name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName());
  }

  @Override
  public String toString() {
    return "Parameter{name='" + getName() + "'" + ", value='" + getValue() + "'" + "}";
  }
}
