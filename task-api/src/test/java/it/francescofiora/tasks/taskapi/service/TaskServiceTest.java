package it.francescofiora.tasks.taskapi.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
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
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class TaskServiceTest {

  private static final Long ID = 1L;
  private static final String ERROR_MSG = "Error message";

  private TaskService taskService;

  @MockBean
  private SequenceGeneratorService sequenceGenerator;

  @MockBean
  private TaskRepository taskRepository;

  @MockBean
  private TaskMapper taskMapper;

  @MockBean
  private JmsProducer jmsProducer;

  private JmsProducer spyJmsProducer;

  /**
   * Set up.
   */
  @BeforeEach
  void setUp() {
    spyJmsProducer = spy(jmsProducer);
    taskService =
        new TaskServiceImpl(taskRepository, taskMapper, sequenceGenerator, spyJmsProducer);
  }

  @Test
  void testCreate() throws Exception {
    var task = new Task();
    when(taskMapper.toEntity(any(NewTaskDto.class))).thenReturn(task);

    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var expected = new TaskDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    var taskDto = new NewTaskDto();
    var actual = taskService.create(taskDto);
    assertThat(actual).isEqualTo(expected);

    verify(spyJmsProducer).send(any(MessageDtoRequestImpl.class));
  }

  @Test
  void testSendError() throws Exception {
    var task = new Task();
    when(taskMapper.toEntity(any(NewTaskDto.class))).thenReturn(task);

    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var jmsProducerErr = mock(JmsProducer.class);
    doThrow(new RuntimeException(ERROR_MSG)).when(jmsProducerErr)
        .send(any(MessageDtoRequest.class));

    var taskServiceErr = new TaskServiceImpl(taskRepository, new TaskMapperImpl(),
        sequenceGenerator, jmsProducerErr);

    var actual = taskServiceErr.create(new NewTaskDto());
    assertThat(actual.getResult().getValue()).isEqualTo(ERROR_MSG);
    assertThat(actual.getStatus()).isEqualTo(TaskStatus.ERROR);
  }

  @Test
  void testPatchNotFound() throws Exception {
    var taskDto = new UpdatableTaskDto();
    assertThrows(NotFoundAlertException.class, () -> taskService.patch(taskDto));
  }

  @Test
  void testPatch() throws Exception {
    var task = new Task();
    when(taskRepository.findById(eq(ID))).thenReturn(Optional.of(task));

    var taskDto = new UpdatableTaskDto();
    taskDto.setId(ID);
    taskService.patch(taskDto);

  }

  @Test
  void testFindAll() throws Exception {
    var task = new Task();
    when(taskRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Task>(singletonList(task)));
    var expected = new TaskDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var page = taskService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var task = new Task();
    task.setId(ID);
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));
    var expected = new TaskDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isPresent();
    var actual = taskOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    taskService.delete(ID);
  }

  @Test
  void testResponse() throws Exception {
    var task = new Task();
    task.setId(ID);
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

    var response = new MessageDtoResponseImpl().taskId(ID).result("Result");
    taskService.response(response);
  }

  @Test
  void testResponseNotFound() throws Exception {
    when(taskRepository.findById(eq(ID))).thenReturn(Optional.empty());
    var response = new MessageDtoResponseImpl().taskId(ID).result("Result");
    assertThrows(JmsException.class, () -> taskService.response(response));
  }
}
