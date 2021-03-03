package it.francescofiora.tasks.taskapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class UpdatableTaskDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    UpdatableTaskDto taskDto1 = new UpdatableTaskDto();
    taskDto1.setId(1L);
    UpdatableTaskDto taskDto2 = new UpdatableTaskDto();
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto2.setId(taskDto1.getId());
    assertThat(taskDto1).isEqualTo(taskDto2);
    taskDto2.setId(2L);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto1.setId(null);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
  }
}
