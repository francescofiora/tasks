package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import it.francescofiora.tasks.taskapi.domain.Task;
import org.junit.jupiter.api.Test;

class TaskMapperTest {

  @Test
  void testEntityFromId() {
    var id = 1L;
    var taskMapper = new TaskMapperImpl();
    assertThat(taskMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(taskMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    var taskMapper = new TaskMapperImpl();
    assertThat(taskMapper.toDto(null)).isNull();
    assertThat(taskMapper.toEntity(null)).isNull();
    assertDoesNotThrow(() -> taskMapper.updateEntityFromDto(null, new Task()));
  }

  @Test
  void testNullFields() {
    var task = new Task();
    task.getParameters().add(null);
    var taskMapper = new TaskMapperImpl();
    var taskDto = taskMapper.toDto(task);
    assertThat(taskDto.getParameters()).hasSize(1);
    assertThat(taskDto.getParameters().iterator().next()).isNull();

    task.setParameters(null);
    taskDto = taskMapper.toDto(task);
    assertThat(taskDto.getParameters()).isNull();
    assertThat(taskDto.getResult()).isNull();

    task = taskMapper.toEntity(taskDto);
    assertThat(task.getParameters()).isNull();
  }
}
