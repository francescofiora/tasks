package it.francescofiora.tasks.taskexecutor.service.dto;

import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import org.junit.jupiter.api.Test;

class ParameterDtoTest {

  @Test
  void dtoEqualsVerifier() {
    var parameterDto1 = TestUtils.createParameterDto();
    var parameterDto2 = TestUtils.createParameterDto();
    TestUtils.checkEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto1.setName("Name2");
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto1.setName(null);
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto1 = TestUtils.createParameterDto();
    parameterDto1.setValue("Value2");
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);

    parameterDto1.setValue(null);
    TestUtils.checkNotEqualHashAndToString(parameterDto1, parameterDto2);
  }
}
