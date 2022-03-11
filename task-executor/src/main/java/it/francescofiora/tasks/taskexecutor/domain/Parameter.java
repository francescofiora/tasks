package it.francescofiora.tasks.taskexecutor.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Parameter Entity.
 */
@Getter
@Setter
@ToString(callSuper = true, includeFieldNames = true)
public class Parameter implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (getName() == null || obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(getName(), ((Parameter) obj).getName());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getName());
  }
}
