package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

public class Parameter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Field("name")
  private String name;

  @Field("value")
  private String value;

  public String getName() {
    return name;
  }

  public Parameter name(String name) {
    this.name = name;
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public Parameter value(String value) {
    this.value = value;
    return this;
  }

  public void setValue(String value) {
    this.value = value;
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
    final int prime = 31;
    int result = 1;
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Parameter{name='" + getName() + "'" + ", value='" + getValue() + "'" + "}";
  }
}
