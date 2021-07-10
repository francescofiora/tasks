package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

class DatabaseSequenceTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    var dbSequence1 = new DatabaseSequence();
    dbSequence1.setId("ID1");

    assertThat(dbSequence1.equals(null)).isFalse();
    assertThat(dbSequence1.equals(new Object())).isFalse();

    var dbSequence2 = new DatabaseSequence();
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence2.setId(dbSequence1.getId());
    TestUtils.checkEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence2.setId("ID2");
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence1.setId(null);
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);
  }
}
