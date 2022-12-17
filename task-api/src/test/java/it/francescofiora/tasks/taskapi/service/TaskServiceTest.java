package it.francescofiora.tasks.taskapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.jms.JmsProducer;
import it.francescofiora.tasks.taskapi.jms.errors.JmsException;
import it.francescofiora.tasks.taskapi.repository.TaskRepository;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import it.francescofiora.tasks.taskapi.service.impl.TaskServiceImpl;
import it.francescofiora.tasks.taskapi.service.mapper.TaskMapper;
import it.francescofiora.tasks.taskapi.service.mapper.TaskMapperImpl;
import it.francescofiora.tasks.taskapi.web.errors.NotFoundAlertException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class TaskServiceTest {

  private static final Long ID = 1L;
  private static final String ERROR_MSG = "Error message";

  @Test
  void testCreate() {
    var task = new Task();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toEntity(any(NewTaskDto.class))).thenReturn(task);

    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var expected = new TaskDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    var taskDto = new NewTaskDto();
    var jmsProducer = mock(JmsProducer.class);
    var taskService = new TaskServiceImpl(taskRepository, taskMapper,
        mock(SequenceGeneratorService.class), jmsProducer);
    var actual = taskService.create(taskDto);
    assertThat(actual).isEqualTo(expected);

    verify(jmsProducer).send(any(MessageDtoRequestImpl.class));
  }

  @Test
  void testSendError() {
    var task = new Task();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toEntity(any(NewTaskDto.class))).thenReturn(task);

    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var jmsProducerErr = mock(JmsProducer.class);
    doThrow(new RuntimeException(ERROR_MSG)).when(jmsProducerErr)
        .send(any(MessageDtoRequest.class));

    var taskServiceErr = new TaskServiceImpl(taskRepository, new TaskMapperImpl(),
        mock(SequenceGeneratorService.class), jmsProducerErr);

    var actual = taskServiceErr.create(new NewTaskDto());
    assertThat(actual.getResult().getValue()).isEqualTo(ERROR_MSG);
    assertThat(actual.getStatus()).isEqualTo(TaskStatus.ERROR);
  }

  @Test
  void testPatchNotFound() {
    var taskDto = new UpdatableTaskDto();
    var taskService = new TaskServiceImpl(mock(TaskRepository.class), mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    assertThrows(NotFoundAlertException.class, () -> taskService.patch(taskDto));
  }

  @Test
  void testPatch() {
    var task = new Task();
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(ID)).thenReturn(Optional.of(task));

    var taskDto = new UpdatableTaskDto();
    taskDto.setId(ID);
    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    taskService.patch(taskDto);
  }

  @Test
  void testFindAll() {
    var task = new Task();
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<Task>(List.of(task)));
    var expected = new TaskDto();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var taskService = new TaskServiceImpl(taskRepository, taskMapper,
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    var page = taskService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() {
    var taskService = new TaskServiceImpl(mock(TaskRepository.class), mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isNotPresent();
  }

  @Test
  void testFindOne() {
    var task = new Task();
    task.setId(ID);
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
    var expected = new TaskDto();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    var taskService = new TaskServiceImpl(taskRepository, taskMapper,
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isPresent();
    var actual = taskOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() {
    var taskService = new TaskServiceImpl(mock(TaskRepository.class), mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    taskService.delete(ID);
  }

  @Test
  void testResponse() {
    var task = new Task();
    task.setId(ID);
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

    var response = new MessageDtoResponseImpl().taskId(ID).result("Result");
    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    taskService.response(response);
  }

  @Test
  void testResponseNotFound() {
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(ID)).thenReturn(Optional.empty());
    var response = new MessageDtoResponseImpl().taskId(ID).result("Result");
    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class),
        mock(SequenceGeneratorService.class), mock(JmsProducer.class));
    assertThrows(JmsException.class, () -> taskService.response(response));
  }
}
