package it.francescofiora.tasks.taskapi.service.dto;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class ParameterDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    ParameterDto parameterDto1 = new ParameterDto();
    parameterDto1.setName("Name1");
    parameterDto1.setValue("Value1");
    ParameterDto parameterDto2 = new ParameterDto();
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto2.setName(parameterDto1.getName());
    parameterDto2.setValue(parameterDto1.getValue());
    TestUtils.checkEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto2.setName("Name2");
    parameterDto2.setValue("Value2");
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto1.setName(null);
    parameterDto1.setValue(null);
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);
  }
}
