package it.francescofiora.tasks.taskexecutor.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TaskMapperTest {

  @Test
  void testNullObject() {
    var taskMapper = new TaskMapperImpl();
    assertThat(taskMapper.toDto(null)).isNull();
    assertThat(taskMapper.taskToRefJobDto(null)).isNull();
    assertThat(taskMapper.taskToRefTaskDto(null)).isNull();
    assertThat(taskMapper.taskToResultDto(null)).isNull();
    assertThat(taskMapper.parameterToParameterDto(null)).isNull();
    assertThat(taskMapper.parameterSetToParameterDtoSet(null)).isNull();
  }
}
