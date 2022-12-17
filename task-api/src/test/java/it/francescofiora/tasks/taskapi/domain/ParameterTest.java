package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

class ParameterTest {

  private static final String NAME_1 = "Name1";
  private static final String NAME_2 = "Name2";

  @Test
  void equalsObjectVerifier() {
    Object parameter1 = TestUtils.createParameter(NAME_1);
    assertThat(parameter1).isNotEqualTo(new Object());
  }

  @Test
  void equalsVerifier() {
    var parameter1 = TestUtils.createParameter(NAME_1);
    Parameter parameter2 = null;

    assertThat(parameter1).isNotEqualTo(parameter2);

    var parameter3 = TestUtils.createParameter(NAME_1);
    TestUtils.checkEqualHashAndToString(parameter1, parameter3);

    parameter3 = TestUtils.createParameter(NAME_2);
    TestUtils.checkNotEqualHashAndToString(parameter1, parameter3);

    parameter1 = TestUtils.createParameter(null);
    TestUtils.checkNotEqualHashAndToString(parameter1, parameter3);
  }
}
