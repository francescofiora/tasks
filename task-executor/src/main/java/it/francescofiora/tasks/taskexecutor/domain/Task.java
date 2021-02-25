package it.francescofiora.tasks.taskexecutor.domain;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Task implements Serializable {

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
  
  @ManyToMany
  @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
  @JoinTable(
      name = "task_parameter",
      joinColumns = @JoinColumn(name = "task_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "paramter_id", referencedColumnName = "id"))
  private Set<Parameter> parameters;

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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Task)) {
      return false;
    }
    return id != null && id.equals(((Task) obj).id);
  }

  @Override
  public String toString() {
    return "Task [id=" + getId() + ", jmsMessageId=" + getJmsMessageId() + ", jobName="
        + getJobName() + ", taskType=" + getTaskType() + ", messageCreated=" + getMessageCreated()
        + ", taskRef=" + getTaskRef() + ", result = " + getResult() + "]";
  }

}
