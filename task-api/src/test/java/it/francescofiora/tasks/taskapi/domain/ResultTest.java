package it.francescofiora.tasks.taskapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ResultTest {

  @Test
  public void equalsVerifier() throws Exception {
    Result result1 = new Result();
    result1.setValue("Result1");
    Result result2 = new Result();
    result2.setValue(result1.getValue());
    assertThat(result1).isEqualTo(result2);
    result2.setValue("Result2");
    assertThat(result1).isNotEqualTo(result2);
    result1.setValue(null);
    assertThat(result1).isNotEqualTo(result2);
  }

}
