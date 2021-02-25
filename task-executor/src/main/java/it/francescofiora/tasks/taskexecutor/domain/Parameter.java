package it.francescofiora.tasks.taskexecutor.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Table(name = "parameter")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Parameter implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;
  
  @Column(name = "value", nullable = false)
  private String value;

  public Parameter name(String name) {
    this.name = name;
    return this;
  }
  
  public Parameter value(String value) {
    this.value = value;
    return this;
  }

  @Override
  public String toString() {
    return "Parameter {id=" + getId() + ", name=" + getName() + ", value=" + getValue() + "}";
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
    if (!(obj instanceof Parameter)) {
      return false;
    }
    return id != null && id.equals(((Parameter) obj).id);
  }
}
