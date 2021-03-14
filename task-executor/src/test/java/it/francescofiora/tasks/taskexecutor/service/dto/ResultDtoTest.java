package it.francescofiora.tasks.taskexecutor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ResultDtoTest {

  private static final String VALUE = "Value";

  @Test
  public void dtoEqualsVerifier() throws Exception {
    ResultDto resultDto1 = new ResultDto();
    resultDto1.setValue("Value1");
    ResultDto resultDto2 = new ResultDto();
    assertThat(resultDto1).isNotEqualTo(resultDto2);
    resultDto2.setValue(resultDto1.getValue());
    assertThat(resultDto1).isEqualTo(resultDto2);
    resultDto2.setValue("Value2");
    assertThat(resultDto1).isNotEqualTo(resultDto2);
    resultDto1.setValue(null);
    assertThat(resultDto1).isNotEqualTo(resultDto2);
  }

  @Test
  public void testConstructor() {
    ResultDto result = new ResultDto(VALUE);
    assertThat(result.getValue()).isEqualTo(VALUE);
  }
}
