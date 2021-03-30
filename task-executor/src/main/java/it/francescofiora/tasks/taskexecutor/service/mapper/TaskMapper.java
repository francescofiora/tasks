package it.francescofiora.tasks.taskexecutor.service.mapper;

import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskExecutorDto}.
 */
@Mapper(componentModel = "spring", uses = {}, imports = TaskType.class)
public interface TaskMapper {

  @Mapping(target = "parameters", ignore = true)
  @Mapping(target = "jobName", ignore = true)
  @Mapping(target = "taskType", ignore = true)
  @Mapping(target = "taskRef", ignore = true)
  @Mapping(target = "jobInstanceId", ignore = true)
  @Mapping(target = "result", ignore = true)
  Task toEntity(TaskExecutorDto taskDto);

  List<Task> toEntity(List<TaskExecutorDto> dtoList);

  @Mapping(source = "taskRef", target = "task.id")
  @Mapping(expression = "java(TaskType.valueOf(task.getTaskType()))", target = "task.type")
  @Mapping(source = "jobInstanceId", target = "job.id")
  @Mapping(source = "jobName", target = "job.jobName")
  @Mapping(source = "result", target = "result.value")
  TaskExecutorDto toDto(Task task);

  List<TaskExecutorDto> toDto(List<Task> entityList);

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
    Task task = new Task();
    task.setId(id);
    return task;
  }
}
