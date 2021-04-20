package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TaskMapperTest {
  private TaskMapper taskMapper;

  @BeforeEach
  void setUp() {
    taskMapper = new TaskMapperImpl();
  }

  @Test
  void testEntityFromId() {
    Long id = 1L;
    assertThat(taskMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(taskMapper.fromId(null)).isNull();
  }

}
