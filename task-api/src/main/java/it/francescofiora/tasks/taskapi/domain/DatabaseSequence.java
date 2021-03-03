package it.francescofiora.tasks.taskapi.domain;

import java.io.Serializable;
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
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public String toString() {
    return "DatabaseSequence{id='" + getId() + "'" + ", seq='" + getSeq() + "'" + "}";
  }
}
