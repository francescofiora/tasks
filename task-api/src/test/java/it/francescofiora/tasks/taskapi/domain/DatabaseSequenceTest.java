package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class DatabaseSequenceTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    DatabaseSequence dbSequence1 = new DatabaseSequence();
    dbSequence1.setId("ID1");
    DatabaseSequence dbSequence2 = new DatabaseSequence();
    assertThat(dbSequence1).isNotEqualTo(dbSequence2);
    dbSequence2.setId(dbSequence1.getId());
    assertThat(dbSequence1).isEqualTo(dbSequence2);
    dbSequence2.setId("ID2");
    assertThat(dbSequence1).isNotEqualTo(dbSequence2);
    dbSequence1.setId(null);
    assertThat(dbSequence1).isNotEqualTo(dbSequence2);
  }
}
