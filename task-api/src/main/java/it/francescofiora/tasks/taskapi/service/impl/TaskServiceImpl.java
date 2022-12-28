package it.francescofiora.tasks.taskapi.service.impl;

import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.domain.Result;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.jms.JmsProducer;
import it.francescofiora.tasks.taskapi.jms.errors.JmsException;
import it.francescofiora.tasks.taskapi.repository.TaskRepository;
import it.francescofiora.tasks.taskapi.service.SequenceGeneratorService;
import it.francescofiora.tasks.taskapi.service.TaskService;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import it.francescofiora.tasks.taskapi.service.mapper.TaskMapper;
import it.francescofiora.tasks.taskapi.web.errors.NotFoundAlertException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Task Service Impl.
 */
@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

  private static final String ENTITY_NAME = "TaskDto";
  private static final GenericPropertyMatcher PROPERTY_MATCHER_DEFAULT =
      GenericPropertyMatchers.contains().ignoreCase();
  private static final GenericPropertyMatcher PROPERTY_MATCHER_EXACT =
      GenericPropertyMatchers.exact();

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final SequenceGeneratorService sequenceGenerator;
  private final JmsProducer jmsProducer;

  @Override
  public TaskDto create(NewTaskDto taskDto) {
    log.debug("Request to create Task : {}", taskDto);
    var task = taskMapper.toEntity(taskDto);
    task.setId(sequenceGenerator.generateSequence(Task.SEQUENCE_NAME));
    task = taskRepository.save(task);

    send(task);
    return taskMapper.toDto(task);
  }

  private void send(Task task) {
    try {
      // @formatter:off
      jmsProducer.send(new MessageDtoRequestImpl()
          .taskId(task.getId())
          .type(task.getType())
          .addParameters(task.getParameters().stream()
              .collect(Collectors.toMap(Parameter::getName, Parameter::getValue))));
      // @formatter:on
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

    var taskOpt = taskRepository.findById(taskDto.getId());
    if (!taskOpt.isPresent()) {
      var id = String.valueOf(taskDto.getId());
      throw new NotFoundAlertException(ENTITY_NAME, id, ENTITY_NAME + " not found with id " + id);
    }
    var task = taskOpt.get();
    taskMapper.updateEntityFromDto(taskDto, task);
    taskRepository.save(task);
  }

  @Override
  public Page<TaskDto> findAll(String description, TaskType type, TaskStatus status,
      Pageable pageable) {
    log.debug("Request to get all Tasks");
    var task = new Task();
    task.setDescription(description);
    task.setType(type);
    task.setStatus(status);
    task.setParameters(null);
    var exampleMatcher = ExampleMatcher.matchingAll()
        .withMatcher("description", PROPERTY_MATCHER_DEFAULT)
        .withMatcher("type", PROPERTY_MATCHER_EXACT).withMatcher("status", PROPERTY_MATCHER_EXACT);
    var example = Example.of(task, exampleMatcher);
    return taskRepository.findAll(example, pageable).map(taskMapper::toDto);
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

    var taskOpt = taskRepository.findById(response.getTaskId());
    if (!taskOpt.isPresent()) {
      throw new JmsException("Not found!");
    }
    var task = taskOpt.get();
    task.setStatus(response.getStatus());
    task.setResult(new Result(response.getResult()));
    taskRepository.save(task);
  }
}
