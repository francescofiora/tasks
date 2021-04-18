package it.francescofiora.tasks.taskexecutor.service.mapper;

import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskExecutorDto}.
 */
@Mapper(componentModel = "spring", uses = {}, imports = TaskType.class)
public interface TaskMapper {

  @Mapping(source = "taskRef", target = "task.id")
  @Mapping(expression = "java(TaskType.valueOf(task.getTaskType()))", target = "task.type")
  @Mapping(source = "jobInstanceId", target = "job.id")
  @Mapping(source = "jobName", target = "job.jobName")
  @Mapping(source = "result", target = "result.value")
  TaskExecutorDto toDto(Task task);
}
