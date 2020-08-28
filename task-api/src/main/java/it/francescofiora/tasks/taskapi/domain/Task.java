package it.francescofiora.tasks.taskapi.domain;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
  
  public Long getId() {
    return id;
  }

  public Task id(Long id) {
    this.id = id;
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public Task description(String description) {
    this.description = description;
    return this;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TaskType getType() {
    return type;
  }

  public Task type(TaskType type) {
    this.type = type;
    return this;
  }

  public void setType(TaskType type) {
    this.type = type;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public Task status(TaskStatus status) {
    this.status = status;
    return this;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public Set<Parameter> getParameters() {
    return parameters;
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

  public void setParameters(Set<Parameter> parameters) {
    this.parameters = parameters;
  }

  public Result getResult() {
    return result;
  }

  public Task result(Result result) {
    this.result = result;
    return this;
  }
  
  public void setResult(Result result) {
    this.result = result;
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
