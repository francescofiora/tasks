package it.francescofiora.tasks.taskapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class TaskDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TaskDto taskDto1 = new TaskDto();
    taskDto1.setId(1L);
    TaskDto taskDto2 = new TaskDto();
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto2.setId(taskDto1.getId());
    assertThat(taskDto1).isEqualTo(taskDto2);
    taskDto2.setId(2L);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto1.setId(null);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
  }
}
