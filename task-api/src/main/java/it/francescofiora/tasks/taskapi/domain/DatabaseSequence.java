package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "database_sequences")
public class DatabaseSequence implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  private long seq;

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "DatabaseSequence{id='" + getId() + "'" + ", seq='" + getSeq() + "'" + "}";
  }
}
