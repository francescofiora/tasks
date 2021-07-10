package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDto}.
 */
@Mapper(componentModel = "spring", uses = {ParameterMapper.class})
public interface TaskMapper {

  TaskDto toDto(Task entity);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "result", ignore = true)
  Task toEntity(NewTaskDto taskDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "result", ignore = true)
  @Mapping(target = "parameters", ignore = true)
  void updateEntityFromDto(UpdatableTaskDto taskDto, @MappingTarget Task task);

  /**
   * new Task from Id.
   *
   * @param id Long
   * @return Task
   */
  default Task fromId(Long id) {
    if (id == null) {
      return null;
    }
    var task = new Task();
    task.setId(id);
    return task;
  }
}
