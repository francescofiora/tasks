package it.francescofiora.tasks.taskapi.domain;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "task")
public class Task implements Serializable {

  private static final long serialVersionUID = 1L;

  @Transient
  public static final String SEQUENCE_NAME = "tasks_sequence";

  @Id
  private Long id;

  @Field("description")
  private String description;

  @Field("type")
  private TaskType type;

  @Field("status")
  private TaskStatus status = TaskStatus.SCHEDULATED;

  @Field("parameter")
  private Set<Parameter> parameters = new HashSet<>();

  @Field("result")
  private Result result;
  
  public Task id(Long id) {
    this.id = id;
    return this;
  }

  public Task description(String description) {
    this.description = description;
    return this;
  }

  public Task type(TaskType type) {
    this.type = type;
    return this;
  }

  public Task status(TaskStatus status) {
    this.status = status;
    return this;
  }

  public Task parameters(Set<Parameter> parameters) {
    this.parameters = parameters;
    return this;
  }

  public Task addParameter(Parameter parameter) {
    this.parameters.add(parameter);
    return this;
  }

  public Task removeParameter(Parameter parameter) {
    this.parameters.remove(parameter);
    return this;
  }

  public Task result(Result result) {
    this.result = result;
    return this;
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Task)) {
      return false;
    }
    return id != null && id.equals(((Task) o).id);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "Task{" + "id=" + getId() + ", description='" + getDescription() + "'" + ", type='"
        + getType() + "'" + ", status='" + getStatus() + "'" + "}";
  }
}
