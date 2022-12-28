package it.francescofiora.tasks.taskexecutor.service.impl;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.repository.TaskRepository;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.service.mapper.TaskMapper;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Task Service Impl.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();
  private static final GenericPropertyMatcher PROPERTY_MATCHER_EXACT =
      GenericPropertyMatchers.exact();

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;

  @Override
  public Task save(Task task) {
    log.debug("Request to save a Task : {}", task);

    return taskRepository.save(task);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Task> findByTaskRef(Long taskRef) {
    log.debug("Request to find a Task : {}", taskRef);
    return taskRepository.findByTaskRef(taskRef);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TaskExecutorDto> findAll(JobType jobName, Long taskRef, String type,
      TaskStatus status, Pageable pageable) {
    log.debug("Request to get all Tasks");
    var task = new Task();
    task.setJobName(jobName);
    task.setTaskRef(taskRef);
    task.setTaskType(type);
    task.setStatus(status);
    task.setParameters(null);
    var exampleMatcher = ExampleMatcher.matchingAll()
        .withMatcher("jobName", PROPERTY_MATCHER_EXACT)
        .withMatcher("taskRef", PROPERTY_MATCHER_EXACT)
        .withMatcher("type", PROPERTY_MATCHER_DEFAULT)
        .withMatcher("status", PROPERTY_MATCHER_EXACT);
    var example = Example.of(task, exampleMatcher);
    return taskRepository.findAll(example, pageable).map(taskMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TaskExecutorDto> findOne(Long id) {
    log.debug("Request to get Task : {}", id);
    return taskRepository.findById(id).map(taskMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Task : {}", id);
    var opt = taskRepository.findById(id);
    if (opt.isPresent()) {
      taskRepository.deleteById(id);
    }
  }
}
