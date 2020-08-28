package it.francescofiora.tasks.taskapi.service.impl;

import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.domain.Result;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.jms.TaskExecutor;
import it.francescofiora.tasks.taskapi.repository.TaskRepository;
import it.francescofiora.tasks.taskapi.service.SequenceGeneratorService;
import it.francescofiora.tasks.taskapi.service.TaskService;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import it.francescofiora.tasks.taskapi.service.mapper.NewTaskMapper;
import it.francescofiora.tasks.taskapi.service.mapper.TaskMapper;
import it.francescofiora.tasks.taskapi.service.mapper.UpdatableTaskDtoTaskMapper;
import it.francescofiora.tasks.taskapi.web.errors.NotFoundAlertException;

import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private static final String ENTITY_NAME = "Task";

  private final SequenceGeneratorService sequenceGenerator;

  private final TaskRepository taskRepository;

  private final TaskMapper taskMapper;
  private final NewTaskMapper newtaskMapper;
  private final UpdatableTaskDtoTaskMapper updatableTaskDtoTaskMapper;

  private final TaskExecutor taskExecutor;

  /**
   * Constructor.
   * 
   * @param taskRepository             TaskRepository
   * @param taskMapper                 TaskMapper
   * @param newtaskMapper              NewTaskMapper
   * @param updatableTaskDtoTaskMapper UpdatableTaskDtoTaskMapper
   * @param sequenceGenerator          SequenceGeneratorService
   * @param taskExecutor               TaskExecutor
   */
  public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper,
      NewTaskMapper newtaskMapper, UpdatableTaskDtoTaskMapper updatableTaskDtoTaskMapper,
      SequenceGeneratorService sequenceGenerator, TaskExecutor taskExecutor) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
    this.newtaskMapper = newtaskMapper;
    this.updatableTaskDtoTaskMapper = updatableTaskDtoTaskMapper;
    this.sequenceGenerator = sequenceGenerator;
    this.taskExecutor = taskExecutor;
  }

  @Override
  public TaskDto create(NewTaskDto taskDto) {
    log.debug("Request to create Task : {}", taskDto);
    Task task = newtaskMapper.toEntity(taskDto);
    task.setId(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME));
    task = taskRepository.save(task);

    send(task);
    return taskMapper.toDto(task);
  }

  private void send(Task task) {
    try {
      taskExecutor.send(new MessageDtoRequestImpl()
          .taskId(task.getId())
          .type(task.getType())
          .addParameters(task.getParameters().stream()
              .collect(Collectors.toMap(Parameter::getName, Parameter::getValue))));
    } catch (Exception e) {
      task.setStatus(TaskStatus.ERROR);
      task.setResult(new Result());
      task.getResult().setValue(e.getMessage());
      taskRepository.save(task);
    }
  }

  @Override
  public void patch(UpdatableTaskDto taskDto) {
    log.debug("Request to patch Task : {}", taskDto);

    Optional<Task> taskOpt = taskRepository.findById(taskDto.getId());
    if (!taskOpt.isPresent()) {
      throw new NotFoundAlertException(ENTITY_NAME);
    }
    Task task = taskOpt.get();
    updatableTaskDtoTaskMapper.updateEntityFromDto(taskDto, task);
    taskRepository.save(task);
  }

  @Override
  public Page<TaskDto> findAll(Pageable pageable) {
    log.debug("Request to get all Tasks");
    return taskRepository.findAll(pageable).map(taskMapper::toDto);
  }

  @Override
  public Optional<TaskDto> findOne(Long id) {
    log.debug("Request to get Task : {}", id);
    return taskRepository.findById(id).map(taskMapper::toDto);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Task : {}", id);
    taskRepository.deleteById(id);
  }

  @Override
  public void response(MessageDtoResponse response) {
    log.debug("Response Task : {}", response);
    
    Optional<Task> taskOpt = taskRepository.findById(response.getTaskId());
    if (!taskOpt.isPresent()) {
      throw new RuntimeException("Not found!");
    }
    Task task = taskOpt.get();
    task.setStatus(response.getStatus());
    task.setResult(new Result().value(response.getResult()));
    taskRepository.save(task);
  }
}
