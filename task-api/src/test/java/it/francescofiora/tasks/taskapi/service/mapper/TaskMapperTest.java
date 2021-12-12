package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.tasks.taskapi.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskMapperTest {
  private TaskMapper taskMapper;

  @BeforeEach
  void setUp() {
    taskMapper = new TaskMapperImpl();
  }

  @Test
  void testEntityFromId() {
    var id = 1L;
    assertThat(taskMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(taskMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(taskMapper.toDto(null)).isNull();

    assertThat(taskMapper.toEntity(null)).isNull();

    assertDoesNotThrow(() -> taskMapper.updateEntityFromDto(null, new Task()));
  }

  @Test
  void testNullFields() {
    var task = new Task();
    task.getParameters().add(null);
    var taskDto = taskMapper.toDto(task);
    assertThat(taskDto.getParameters()).hasSize(1);
    assertThat(taskDto.getParameters().iterator().next()).isEqualTo(null);

    task.setParameters(null);
    taskDto = taskMapper.toDto(task);
    assertThat(taskDto.getParameters()).isNull();
    assertThat(taskDto.getResult()).isNull();

    task = taskMapper.toEntity(taskDto);
    assertThat(task.getParameters()).isNull();
  }
}
