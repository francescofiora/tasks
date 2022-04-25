package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

class DatabaseSequenceTest {

  private static final String ID_1 = "ID1";
  private static final String ID_2 = "ID2";

  @Test
  void dtoEqualsVerifier() throws Exception {
    var dbSequence1 = TestUtils.createDatabaseSequence(ID_1);
    assertThat(dbSequence1).isNotEqualTo(new Object());

    Object dbSequence2 = null;
    assertThat(dbSequence1).isNotEqualTo(dbSequence2);

    var dbSequence3 = TestUtils.createDatabaseSequence(null);
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence3);

    dbSequence3 = TestUtils.createDatabaseSequence(ID_1);
    TestUtils.checkEqualHashAndToString(dbSequence1, dbSequence3);

    dbSequence3 = TestUtils.createDatabaseSequence(ID_2);
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence3);

    dbSequence1 = TestUtils.createDatabaseSequence(null);
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence3);
  }
}
