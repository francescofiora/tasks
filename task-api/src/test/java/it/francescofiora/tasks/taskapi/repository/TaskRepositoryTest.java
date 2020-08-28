package it.francescofiora.tasks.taskapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.domain.Result;
import it.francescofiora.tasks.taskapi.domain.Task;

public class TaskRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TaskRepository taskRepository;
  
  @Test
  public void testCRUD() throws Exception {
    Task expecteted1 = createTask1();
    Task expecteted2 = createTask2();
    taskRepository.save(expecteted1);
    taskRepository.save(expecteted2);
    
    Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (Task actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(assertEquals(expecteted1, actual)
          || assertEquals(expecteted2, actual)).isTrue();
    }

    Task expecteted3 = createTask3();
    Task task = tasks.getContent().get(0);
    task.setDescription(expecteted3.getDescription());
    task.setStatus(expecteted3.getStatus());
    task.setType(expecteted3.getType());
    task.getResult().setValue(expecteted3.getResult().getValue());
    taskRepository.save(task);

    Optional<Task> optional = taskRepository.findById(task.getId());
    assertThat(optional).isPresent();
    task = optional.get();
    assertThat(assertEquals(expecteted3, task)).isTrue();

    for (Task actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }
  
  private boolean assertEquals(Task expecteted, Task task) {
    return expecteted.getDescription().equals(task.getDescription())
        && expecteted.getStatus().equals(task.getStatus())
        && expecteted.getType().equals(task.getType())
        && expecteted.getResult().equals(task.getResult());
  }

  private Task createTask1() {
    return new Task()
        .id(generateSequence(Task.SEQUENCE_NAME))
        .description("first")
        .status(TaskStatus.SCHEDULATED)
        .type(TaskType.SHORT)
        .result(new Result().value("result 1"))
        .addParameter(new Parameter().name("name").value("value"));
  }

  private Task createTask2() {
    return new Task()
        .id(generateSequence(Task.SEQUENCE_NAME))
        .description("second")
        .status(TaskStatus.SCHEDULATED)
        .type(TaskType.LONG)
        .result(new Result().value("result 2"))
        .addParameter(new Parameter().name("key").value("value"));
  }

  private Task createTask3() {
    return new Task()
        .id(generateSequence(Task.SEQUENCE_NAME))
        .description("third")
        .status(TaskStatus.SCHEDULATED)
        .type(TaskType.NEW_TYPE)
        .result(new Result().value("result 3"))
        .addParameter(new Parameter().name("par").value("value"));
  }
}
