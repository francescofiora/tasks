package it.francescofiora.tasks.taskexecutor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.repository.ParameterRepository;
import it.francescofiora.tasks.taskexecutor.repository.TaskRepository;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.service.impl.TaskServiceImpl;
import it.francescofiora.tasks.taskexecutor.service.mapper.TaskMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
  private static final Long TASK_REF = 10L;

  private TaskService taskService;

  @MockBean
  private ParameterRepository parameterRepositor;

  @MockBean
  private TaskRepository taskRepository;

  @MockBean
  private TaskMapper taskMapper;

  @BeforeEach
  void setUp() {
    taskService = new TaskServiceImpl(taskRepository, parameterRepositor, taskMapper);
  }

  @Test
  void testCreate() throws Exception {
    Task task = new Task();
    task.setId(ID);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Task actual = taskService.save(task);
    assertThat(actual.getId()).isEqualTo(ID);
  }

  @Test
  void testFindByTaskRef() throws Exception {
    Task task = new Task();
    task.setId(ID);
    when(taskRepository.findByTaskRef(TASK_REF)).thenReturn(Optional.of(task));

    Optional<Task> opt = taskService.findByTaskRef(TASK_REF);
    assertThat(opt).isPresent();
    assertThat(opt.get().getId()).isEqualTo(ID);
  }

  @Test
  void testFindAll() throws Exception {
    Task task = new Task();
    when(taskRepository.findAll(any(Pageable.class)))
        .thenReturn(new PageImpl<Task>(List.of(task)));
    TaskExecutorDto expected = new TaskExecutorDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);
    Pageable pageable = PageRequest.of(1, 1);
    Page<TaskExecutorDto> page = taskService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    Optional<TaskExecutorDto> taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    Task task = new Task();
    task.setId(ID);
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));
    TaskExecutorDto expected = new TaskExecutorDto();
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    Optional<TaskExecutorDto> taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isPresent();
    TaskExecutorDto actual = taskOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    Task task = new Task();
    task.setId(ID);
    task.setParameters(Set.of(new Parameter()));
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

    taskService.delete(ID);
    verify(taskRepository).deleteById(eq(ID));
    verify(parameterRepositor).deleteAll(eq(task.getParameters()));
  }

  @Test
  void testDeleteNope() throws Exception {
    when(taskRepository.findById(eq(ID))).thenReturn(Optional.empty());

    taskService.delete(ID);
    verify(taskRepository, times(0)).deleteById(eq(ID));
    verify(parameterRepositor, times(0)).deleteAll(anyIterable());
  }
}
