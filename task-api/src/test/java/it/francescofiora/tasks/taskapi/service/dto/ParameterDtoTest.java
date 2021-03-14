package it.francescofiora.tasks.taskapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ParameterDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    ParameterDto parameterDto1 = new ParameterDto();
    parameterDto1.setName("Name1");
    parameterDto1.setValue("Value1");
    ParameterDto parameterDto2 = new ParameterDto();
    assertThat(parameterDto1).isNotEqualTo(parameterDto2);
    parameterDto2.setName(parameterDto1.getName());
    parameterDto2.setValue(parameterDto1.getValue());
    assertThat(parameterDto1).isEqualTo(parameterDto2);
    parameterDto2.setName("Name2");
    parameterDto2.setValue("Value2");
    assertThat(parameterDto1).isNotEqualTo(parameterDto2);
    parameterDto1.setName(null);
    parameterDto1.setValue(null);
    assertThat(parameterDto1).isNotEqualTo(parameterDto2);
  }
}
