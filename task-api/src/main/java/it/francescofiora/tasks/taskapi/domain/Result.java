package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Field;

public class Result implements Serializable {

  private static final long serialVersionUID = 1L;

  @Field("value")
  private String value;

  public String getValue() {
    return value;
  }

  public Result value(String value) {
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
    if (!(o instanceof Result)) {
      return false;
    }
    return value != null && value.equals(((Result) o).value);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Result {value=" + value + "}";
  }
}
