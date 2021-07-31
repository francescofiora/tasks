package it.francescofiora.tasks.taskexecutor.service;

import it.francescofiora.tasks.taskexecutor.domain.Task;
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

  Page<TaskExecutorDto> findAll(Pageable pageable);

  Optional<TaskExecutorDto> findOne(Long id);

  void delete(Long id);
}
