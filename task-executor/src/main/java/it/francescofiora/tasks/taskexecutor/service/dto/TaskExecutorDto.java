package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.francescofiora.tasks.message.enumeration.TaskStatus;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class TaskExecutorDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Task Execution identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @Schema(description = "Task", required = true)
  @JsonProperty("task")
  private RefTaskDto task;
  
  @Schema(description = "JMS Unique identifier", required = true)
  @JsonProperty("jmsMessageId")
  private String jmsMessageId;

  @Schema(description = "Job", required = true)
  @JsonProperty("job")
  private RefJobDto job = new RefJobDto();
  
  @Schema(description = "status of task", example = "TERMINATED", required = true)
  @JsonProperty("status")
  private TaskStatus status;

  @Schema(description = "messageCreated", required = true)
  @JsonProperty("messageCreated")
  private Timestamp messageCreated;
  
  @Schema(description = "result of execution", required = false)
  @JsonProperty("result")
  @Valid
  private ResultDto result;

  @Schema(required = true)
  @JsonProperty("parameters")
  @Valid
  private Set<ParameterDto> parameters = new HashSet<>();

  @NotBlank
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotBlank
  public String getJmsMessageId() {
    return jmsMessageId;
  }

  public void setJmsMessageId(String jmsMessageId) {
    this.jmsMessageId = jmsMessageId;
  }

  @NotBlank
  public RefJobDto getJob() {
    return job;
  }

  public void setJob(RefJobDto job) {
    this.job = job;
  }

  @NotBlank
  public Timestamp getMessageCreated() {
    return messageCreated;
  }

  public void setMessageCreated(Timestamp messageCreated) {
    this.messageCreated = messageCreated;
  }

  @NotBlank
  public RefTaskDto getTask() {
    return task;
  }

  public void setTask(RefTaskDto task) {
    this.task = task;
  }

  @NotNull
  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }

  public ResultDto getResult() {
    return result;
  }

  public void setResult(ResultDto result) {
    this.result = result;
  }

  @NotNull
  public Set<ParameterDto> getParameters() {
    return parameters;
  }

  public void setParameters(Set<ParameterDto> parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TaskExecutorDto taskDto = (TaskExecutorDto) o;
    if (taskDto.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), taskDto.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "TaskDto{id='" + getId() + "', task='" + getTask() + "', status='" + getStatus() + "'}";
  }
}
