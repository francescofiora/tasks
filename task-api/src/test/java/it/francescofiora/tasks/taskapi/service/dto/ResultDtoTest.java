package it.francescofiora.tasks.taskapi.service.dto;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class ResultDtoTest {

  @Test
  void dtoEqualsVerifier() throws Exception {
    ResultDto resultDto1 = new ResultDto();
    resultDto1.setValue("Value1");
    ResultDto resultDto2 = new ResultDto();
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);

    resultDto2.setValue(resultDto1.getValue());
    TestUtils.checkEqualHashAndToString(resultDto1, resultDto2);

    resultDto2.setValue("Value2");
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);

    resultDto1.setValue(null);
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);
  }
}
