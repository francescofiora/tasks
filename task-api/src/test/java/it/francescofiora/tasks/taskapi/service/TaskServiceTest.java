package it.francescofiora.tasks.taskapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.jms.TaskExecutor;
import it.francescofiora.tasks.taskapi.repository.TaskRepository;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import it.francescofiora.tasks.taskapi.service.impl.TaskServiceImpl;
import it.francescofiora.tasks.taskapi.service.mapper.NewTaskMapper;
import it.francescofiora.tasks.taskapi.service.mapper.TaskMapper;
import it.francescofiora.tasks.taskapi.service.mapper.UpdatableTaskDtoTaskMapper;
import it.francescofiora.tasks.taskapi.web.errors.NotFoundAlertException;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

  private static final Long ID = 1L;
  
  private TaskService taskService;

  @MockBean
  private SequenceGeneratorService sequenceGenerator;

  @MockBean
  private TaskRepository taskRepository;

  @MockBean
  private TaskMapper taskMapper;

  @MockBean
  private NewTaskMapper newtaskMapper;

  @MockBean
  private UpdatableTaskDtoTaskMapper updatableTaskDtoTaskMapper;

  @MockBean
  private TaskExecutor taskExecutor;

  private TaskExecutor spyTaskExecutor;
  
  @BeforeEach
  public void setUp() {
    spyTaskExecutor = Mockito.spy(taskExecutor);
    taskService = new TaskServiceImpl(taskRepository, taskMapper, newtaskMapper,
        updatableTaskDtoTaskMapper, sequenceGenerator, spyTaskExecutor);
  }

  @Test
  void testCreate() throws Exception {
    Task task = new Task();
    Mockito.when(newtaskMapper.toEntity(Mockito.any(NewTaskDto.class))).thenReturn(task);

    Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);
    
    TaskDto expected = new TaskDto();
    Mockito.when(taskMapper.toDto(Mockito.any(Task.class))).thenReturn(expected);
    
    NewTaskDto taskDto = new NewTaskDto();
    TaskDto actual = taskService.create(taskDto);
    assertThat(actual).isEqualTo(expected);
    
    Mockito.verify(spyTaskExecutor).send(Mockito.any(MessageDtoRequestImpl.class));
  }

  @Test
  public void testPatchNotFound() throws Exception {
    UpdatableTaskDto taskDto = new UpdatableTaskDto();
    Assertions.assertThrows(NotFoundAlertException.class, () -> taskService.patch(taskDto));
  }

  @Test
  void testPatch() throws Exception {
    Task task = new Task();
    Mockito.when(taskRepository.findById(Mockito.eq(ID))).thenReturn(Optional.of(task));

    UpdatableTaskDto taskDto = new UpdatableTaskDto();
    taskDto.setId(ID);
    taskService.patch(taskDto);

  }

  @Test
  void testFindAll() throws Exception {
    Task task = new Task();
    Mockito.when(taskRepository.findAll(Mockito.any(Pageable.class)))
        .thenReturn(new PageImpl<Task>(Collections.singletonList(task)));
    TaskDto expected = new TaskDto();
    Mockito.when(taskMapper.toDto(Mockito.any(Task.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<TaskDto> page = taskService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  public void testFindOneNotFound() throws Exception {
    Optional<TaskDto> taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    Task task = new Task();
    task.setId(ID);
    Mockito.when(taskRepository.findById(Mockito.eq(task.getId())))
        .thenReturn(Optional.of(task));
    TaskDto expected = new TaskDto();
    Mockito.when(taskMapper.toDto(Mockito.any(Task.class))).thenReturn(expected);

    Optional<TaskDto> taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isPresent();
    TaskDto actual = taskOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    taskService.delete(ID);
  }

  @Test
  void testResponse() throws Exception {
    Task task = new Task();
    task.setId(ID);
    Mockito.when(taskRepository.findById(Mockito.eq(task.getId())))
        .thenReturn(Optional.of(task));

    MessageDtoResponse response = new MessageDtoResponseImpl()
        .taskId(ID)
        .result("Result");
    taskService.response(response);
  }
}
