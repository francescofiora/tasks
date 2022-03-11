package it.francescofiora.tasks.taskexecutor.domain;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.converter.JsonToSetConverter;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Task Entity.
 */
@Getter
@Setter
@Entity
@Table(name = "task")
@ToString(callSuper = true, includeFieldNames = true)
public class Task extends AbstractDomain implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "jms_message_id", nullable = false)
  private String jmsMessageId;

  @Enumerated(EnumType.STRING)
  @Column(name = "job_name", nullable = false)
  private JobType jobName;

  @Column(name = "task_type", nullable = false)
  private String taskType;

  @Column(name = "message_created", nullable = false)
  private Timestamp messageCreated;

  @Column(name = "task_ref", nullable = false, unique = true)
  private Long taskRef;

  @Column(name = "job_instance_id", nullable = false)
  private Long jobInstanceId;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 25)
  private TaskStatus status;

  @Lob
  @Column(name = "parameters")
  @Convert(converter = JsonToSetConverter.class)
  private Set<Parameter> parameters = new HashSet<>();

  @Column(name = "result")
  private String result;

  public Task jmsMessageId(String jmsMessageId) {
    this.jmsMessageId = jmsMessageId;
    return this;
  }

  public Task jobName(JobType jobName) {
    this.jobName = jobName;
    return this;
  }

  public Task taskType(String taskType) {
    this.taskType = taskType;
    return this;
  }

  public Task messageCreated(Timestamp messageCreated) {
    this.messageCreated = messageCreated;
    return this;
  }

  public Task taskRef(Long taskRef) {
    this.taskRef = taskRef;
    return this;
  }

  public Task jobInstanceId(Long jobInstanceId) {
    this.jobInstanceId = jobInstanceId;
    return this;
  }

  public Task status(TaskStatus status) {
    this.status = status;
    return this;
  }

  public Task result(String result) {
    this.result = result;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
