package it.francescofiora.tasks.taskexecutor.service;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Task Service.
 */
public interface TaskService {
  Task save(Task task);

  Optional<Task> findByTaskRef(Long taskRef);

  Page<TaskExecutorDto> findAll(JobType jobName, Long taskRef, String type, TaskStatus status,
      Pageable pageable);

  Optional<TaskExecutorDto> findOne(Long id);

  void delete(Long id);
}
