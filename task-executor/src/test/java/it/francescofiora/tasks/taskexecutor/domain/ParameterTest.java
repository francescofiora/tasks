package it.francescofiora.tasks.taskexecutor.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ParameterTest {

  @Test
  public void equalsVerifier() throws Exception {
    Parameter parameter1 = new Parameter();
    parameter1.setId(1L);
    Parameter parameter2 = new Parameter();
    parameter2.setId(parameter1.getId());
    assertThat(parameter1).isEqualTo(parameter2);
    parameter2.setId(2L);
    assertThat(parameter1).isNotEqualTo(parameter2);
    parameter1.setId(null);
    assertThat(parameter1).isNotEqualTo(parameter2);
  }
}
