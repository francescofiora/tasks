package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ParameterTest {

  @Test
  public void equalsVerifier() throws Exception {
    Parameter parameter1 = new Parameter();
    parameter1.setName("Name1");
    Parameter parameter2 = new Parameter();
    parameter2.setName(parameter1.getName());
    assertThat(parameter1).isEqualTo(parameter2);
    parameter2.setName("Name2");
    assertThat(parameter1).isNotEqualTo(parameter2);
    parameter1.setName(null);
    assertThat(parameter1).isNotEqualTo(parameter2);
  }
  
}
