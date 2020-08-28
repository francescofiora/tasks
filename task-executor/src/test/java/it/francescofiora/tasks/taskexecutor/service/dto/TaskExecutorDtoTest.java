package it.francescofiora.tasks.taskexecutor.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import it.francescofiora.tasks.taskexecutor.TestUtil;

public class TaskExecutorDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(TaskExecutorDto.class);
    TaskExecutorDto taskDto1 = new TaskExecutorDto();
    taskDto1.setId(1L);
    TaskExecutorDto taskDto2 = new TaskExecutorDto();
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto2.setId(taskDto1.getId());
    assertThat(taskDto1).isEqualTo(taskDto2);
    taskDto2.setId(2L);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
    taskDto1.setId(null);
    assertThat(taskDto1).isNotEqualTo(taskDto2);
  }
}
