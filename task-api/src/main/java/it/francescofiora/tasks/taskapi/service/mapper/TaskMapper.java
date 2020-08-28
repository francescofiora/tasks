package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper extends EntityMapper<TaskDto, Task> {

  @Mapping(target = "parameters", ignore = true)
  @Mapping(target = "removeParameter", ignore = true)
  Task toEntity(TaskDto taskDto);

  /**
   * new Task from Id.
   * @param id Long
   * @return Task
   */
  default Task fromId(Long id) {
    if (id == null) {
      return null;
    }
    Task task = new Task();
    task.setId(id);
    return task;
  }
}
