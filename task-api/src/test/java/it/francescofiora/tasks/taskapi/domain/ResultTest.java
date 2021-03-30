package it.francescofiora.tasks.taskapi.domain;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class ResultTest {

  @Test
  public void equalsVerifier() throws Exception {
    Result result1 = new Result();
    result1.setValue("Result1");
    Result result2 = new Result();
    result2.setValue(result1.getValue());
    TestUtils.checkEqualHashAndToString(result1, result2);

    result2.setValue("Result2");
    TestUtils.checkNotEqualHashAndToString(result1, result2);

    result1.setValue(null);
    TestUtils.checkNotEqualHashAndToString(result1, result2);
  }

}
