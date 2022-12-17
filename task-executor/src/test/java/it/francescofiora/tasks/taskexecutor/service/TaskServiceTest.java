package it.francescofiora.tasks.taskexecutor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.repository.TaskRepository;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.service.impl.TaskServiceImpl;
import it.francescofiora.tasks.taskexecutor.service.mapper.TaskMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

class TaskServiceTest {

  private static final Long ID = 1L;
  private static final Long TASK_REF = 10L;

  @Test
  void testCreate() throws Exception {
    var task = new Task();
    task.setId(ID);
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class));
    var actual = taskService.save(task);
    assertThat(actual.getId()).isEqualTo(ID);
  }

  @Test
  void testFindByTaskRef() throws Exception {
    var task = new Task();
    task.setId(ID);
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findByTaskRef(TASK_REF)).thenReturn(Optional.of(task));

    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class));
    var opt = taskService.findByTaskRef(TASK_REF);
    assertThat(opt).isPresent();
    assertThat(opt.get().getId()).isEqualTo(ID);
  }

  @Test
  void testFindAll() throws Exception {
    var task = new Task();
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<Task>(List.of(task)));
    var expected = new TaskExecutorDto();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);
    var pageable = PageRequest.of(1, 1);
    var taskService = new TaskServiceImpl(taskRepository, taskMapper);
    var page = taskService.findAll(pageable);
    assertThat(page.getContent().get(0)).isEqualTo(expected);
  }

  @Test
  void testFindOneNotFound() throws Exception {
    var taskService = new TaskServiceImpl(mock(TaskRepository.class), mock(TaskMapper.class));
    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isNotPresent();
  }

  @Test
  void testFindOne() throws Exception {
    var task = new Task();
    task.setId(ID);
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));
    var expected = new TaskExecutorDto();
    var taskMapper = mock(TaskMapper.class);
    when(taskMapper.toDto(any(Task.class))).thenReturn(expected);

    var taskService = new TaskServiceImpl(taskRepository, taskMapper);
    var taskOpt = taskService.findOne(ID);
    assertThat(taskOpt).isPresent();
    var actual = taskOpt.get();
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void testDelete() throws Exception {
    var task = new Task();
    task.setId(ID);
    task.setParameters(Set.of(new Parameter()));
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(eq(task.getId()))).thenReturn(Optional.of(task));

    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class));
    taskService.delete(ID);
    verify(taskRepository).deleteById(eq(ID));
  }

  @Test
  void testDeleteNope() throws Exception {
    var taskRepository = mock(TaskRepository.class);
    when(taskRepository.findById(eq(ID))).thenReturn(Optional.empty());

    var taskService = new TaskServiceImpl(taskRepository, mock(TaskMapper.class));
    taskService.delete(ID);
    verify(taskRepository, times(0)).deleteById(eq(ID));
  }
}
