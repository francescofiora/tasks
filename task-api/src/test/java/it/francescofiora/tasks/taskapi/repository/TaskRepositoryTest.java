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
    Task expected1 = createTask1();
    Task expected2 = createTask2();
    taskRepository.save(expected1);
    taskRepository.save(expected2);
    
    Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (Task actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(assertEquals(expected1, actual)
          || assertEquals(expected2, actual)).isTrue();
    }

    Task expected3 = createTask3();
    Task task = tasks.getContent().get(0);
    task.setDescription(expected3.getDescription());
    task.setStatus(expected3.getStatus());
    task.setType(expected3.getType());
    task.getResult().setValue(expected3.getResult().getValue());
    taskRepository.save(task);

    Optional<Task> optional = taskRepository.findById(task.getId());
    assertThat(optional).isPresent();
    task = optional.get();
    assertThat(assertEquals(expected3, task)).isTrue();

    for (Task actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }
  
  private boolean assertEquals(Task expected, Task task) {
    return expected.getDescription().equals(task.getDescription())
        && expected.getStatus().equals(task.getStatus())
        && expected.getType().equals(task.getType())
        && expected.getResult().equals(task.getResult());
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
