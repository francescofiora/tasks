package it.francescofiora.tasks.taskapi.domain;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class DatabaseSequenceTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    DatabaseSequence dbSequence1 = new DatabaseSequence();
    dbSequence1.setId("ID1");
    DatabaseSequence dbSequence2 = new DatabaseSequence();
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence2.setId(dbSequence1.getId());
    TestUtils.checkEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence2.setId("ID2");
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);

    dbSequence1.setId(null);
    TestUtils.checkNotEqualHashAndToString(dbSequence1, dbSequence2);
  }
}
