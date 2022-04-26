package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

class ResultTest {

  private static final String VALUE_1 = "Result1";
  private static final String VALUE_2 = "Result2";

  @Test
  void equalsObjectVerifier() throws Exception {
    Object result1 = TestUtils.createResult(VALUE_1);
    assertThat(result1).isNotEqualTo(new Object());
  }

  @Test
  void equalsVerifier() throws Exception {
    var result1 = TestUtils.createResult(VALUE_1);
    Result result2 = null;

    assertThat(result1).isNotEqualTo(result2);

    var result3 = TestUtils.createResult(VALUE_1);
    TestUtils.checkEqualHashAndToString(result1, result3);

    result3 = TestUtils.createResult(VALUE_2);
    TestUtils.checkNotEqualHashAndToString(result1, result3);

    result1 = TestUtils.createResult(null);
    TestUtils.checkNotEqualHashAndToString(result1, result3);
  }
}
