package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
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
    Long id = 1L;
    assertThat(taskMapper.fromId(id).getId()).isEqualTo(id);
    assertThat(taskMapper.fromId(null)).isNull();
  }

  @Test
  void testNullObject() {
    assertThat(taskMapper.toDto(null)).isNull();

    assertThat(taskMapper.toEntity(null)).isNull();
  }

  @Test
  void testNullFields() {
    Task task = new Task();
    task.getParameters().add(null);
    TaskDto taskDto = taskMapper.toDto(task);
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
