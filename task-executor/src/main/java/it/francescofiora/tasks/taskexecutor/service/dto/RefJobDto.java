package it.francescofiora.tasks.taskexecutor.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class RefJobDto implements Serializable {

  private static final long serialVersionUID = 1L;

  @Schema(description = "Unique Job identifier", example = "1", required = true)
  @JsonProperty("id")
  private Long id;

  @Schema(description = "Job name", required = true)
  @JsonProperty("jobName")
  private JobType jobName;

  public RefJobDto() {
  }

  /**
   * constructor.
   * 
   * @param id      Long
   * @param jobName JobType
   */
  public RefJobDto(Long id, JobType jobName) {
    super();
    this.id = id;
    this.jobName = jobName;
  }

  @NotNull
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @NotNull
  public JobType getJobName() {
    return jobName;
  }

  public void setJobName(JobType jobName) {
    this.jobName = jobName;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RefJobDto other = (RefJobDto) o;
    if (other.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), other.getId());
  }

  @Override
  public String toString() {
    return "RefJobDto {id=" + getId() + ", jobName=" + getJobName() + "}";
  }
}
