package it.francescofiora.tasks.taskapi.domain;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Task Document.
 */
@Getter
@Setter
@Document(collection = "task")
@ToString(callSuper = true, includeFieldNames = true)
public class Task extends AbstractDomain implements Serializable {

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
}
