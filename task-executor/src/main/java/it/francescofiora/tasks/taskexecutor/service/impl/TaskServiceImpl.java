package it.francescofiora.tasks.taskexecutor.service.impl;

import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.repository.ParameterRepository;
import it.francescofiora.tasks.taskexecutor.repository.TaskRepository;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.service.mapper.TaskMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  private final TaskMapper taskMapper;

  private final TaskRepository taskRepository;
  private final ParameterRepository parameterRepository;

  /**
   * Constructor.
   *
   * @param taskRepository TaskRepository
   * @param parameterRepository ParameterRepository
   * @param taskMapper TaskMapper
   */
  public TaskServiceImpl(TaskRepository taskRepository, ParameterRepository parameterRepository,
      TaskMapper taskMapper) {
    this.taskRepository = taskRepository;
    this.parameterRepository = parameterRepository;
    this.taskMapper = taskMapper;
  }

  @Override
  public Task save(Task task) {
    log.debug("Request to save a Task : {}", task);

    parameterRepository.saveAll(task.getParameters());

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
  public Page<TaskExecutorDto> findAll(Pageable pageable) {
    log.debug("Request to get all Tasks");
    return taskRepository.findAll(pageable).map(taskMapper::toDto);
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
    Optional<Task> opt = taskRepository.findById(id);
    if (opt.isPresent()) {
      taskRepository.deleteById(id);
      parameterRepository.deleteAll(opt.get().getParameters());
    }
  }

}
