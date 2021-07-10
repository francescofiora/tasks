package it.francescofiora.tasks.taskexecutor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import org.junit.jupiter.api.Test;

class ResultDtoTest {

  private static final String VALUE = "Value";

  @Test
  void dtoEqualsVerifier() throws Exception {
    var resultDto1 = new ResultDto();
    resultDto1.setValue(VALUE);
    var resultDto2 = new ResultDto();
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);

    resultDto2.setValue(VALUE);
    TestUtils.checkEqualHashAndToString(resultDto1, resultDto2);

    resultDto2.setValue("DiffValue");
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);

    resultDto1.setValue(null);
    TestUtils.checkNotEqualHashAndToString(resultDto1, resultDto2);
  }

  @Test
  void testConstructor() {
    var result = new ResultDto(VALUE);
    assertThat(result.getValue()).isEqualTo(VALUE);
  }
}
