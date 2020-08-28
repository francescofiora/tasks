package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Task} and its DTO {@link UpdatableTaskDto}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface UpdatableTaskDtoTaskMapper extends EntityMapper<UpdatableTaskDto, Task> {

  @Mapping(target = "type", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "result", ignore = true)
  @Mapping(target = "parameters", ignore = true)
  @Mapping(target = "removeParameter", ignore = true)
  Task toEntity(UpdatableTaskDto taskDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "result", ignore = true)
  @Mapping(target = "parameters", ignore = true)
  @Mapping(target = "removeParameter", ignore = true)
  void updateEntityFromDto(UpdatableTaskDto taskDto, @MappingTarget Task task);

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
