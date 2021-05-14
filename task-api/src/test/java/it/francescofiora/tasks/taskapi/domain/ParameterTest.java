package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

class ParameterTest {

  @Test
  void equalsVerifier() throws Exception {
    Parameter parameter1 = new Parameter();
    parameter1.setName("Name1");

    assertThat(parameter1.equals(null)).isFalse();
    assertThat(parameter1.equals(new Object())).isFalse();

    Parameter parameter2 = new Parameter();
    parameter2.setName(parameter1.getName());
    TestUtils.checkEqualHashAndToString(parameter1, parameter2);

    parameter2.setName("Name2");
    TestUtils.checkNotEqualHashAndToString(parameter1, parameter2);

    parameter1.setName(null);
    TestUtils.checkNotEqualHashAndToString(parameter1, parameter2);
  }
}
