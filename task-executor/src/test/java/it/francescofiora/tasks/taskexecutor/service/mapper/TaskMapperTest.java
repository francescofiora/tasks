package it.francescofiora.tasks.taskexecutor.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskMapperTest {
  private TaskMapper taskMapper;

  @BeforeEach
  void setUp() {
    taskMapper = new TaskMapperImpl();
  }

  @Test
  void testNullObject() {
    assertThat(taskMapper.toDto(null)).isNull();
  }
}
