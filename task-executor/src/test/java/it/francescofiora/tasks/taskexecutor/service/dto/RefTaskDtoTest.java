package it.francescofiora.tasks.taskexecutor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.francescofiora.tasks.taskexecutor.TestUtil;

public class RefTaskDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(RefTaskDto.class);
    RefTaskDto refTaskDto1 = new RefTaskDto();
    refTaskDto1.setId(1L);
    RefTaskDto refTaskDto2 = new RefTaskDto();
    assertThat(refTaskDto1).isNotEqualTo(refTaskDto2);
    refTaskDto2.setId(refTaskDto1.getId());
    assertThat(refTaskDto1).isEqualTo(refTaskDto2);
    refTaskDto2.setId(2L);
    assertThat(refTaskDto1).isNotEqualTo(refTaskDto2);
    refTaskDto1.setId(null);
    assertThat(refTaskDto1).isNotEqualTo(refTaskDto2);
  }
}
