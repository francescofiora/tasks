package it.francescofiora.tasks.taskexecutor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
public class RefJobDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    RefJobDto refJobDto1 = new RefJobDto();
    refJobDto1.setId(1L);
    RefJobDto refJobDto2 = new RefJobDto();
    assertThat(refJobDto1).isNotEqualTo(refJobDto2);
    refJobDto2.setId(refJobDto1.getId());
    assertThat(refJobDto1).isEqualTo(refJobDto2);
    refJobDto2.setId(2L);
    assertThat(refJobDto1).isNotEqualTo(refJobDto2);
    refJobDto1.setId(null);
    assertThat(refJobDto1).isNotEqualTo(refJobDto2);
  }
}
