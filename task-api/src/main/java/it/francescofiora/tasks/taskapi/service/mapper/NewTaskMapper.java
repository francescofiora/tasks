package it.francescofiora.tasks.taskapi.service.mapper;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Task} and its DTO {@link NewTaskDto}.
 */
@Mapper(componentModel = "spring", uses = { ParameterMapper.class })
public interface NewTaskMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "result", ignore = true)
  Task toEntity(NewTaskDto taskDto);

}
