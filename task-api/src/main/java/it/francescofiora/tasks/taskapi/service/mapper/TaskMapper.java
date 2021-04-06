package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TaskMapper {

  TaskDto toDto(Task entity);
  
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
