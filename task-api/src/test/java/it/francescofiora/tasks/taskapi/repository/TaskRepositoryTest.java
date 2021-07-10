package it.francescofiora.tasks.taskapi.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class TaskRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void testCrud() throws Exception {
    var expected1 = TestUtils.createTask1(generateSequence(Task.SEQUENCE_NAME));
    var expected2 = TestUtils.createTask2(generateSequence(Task.SEQUENCE_NAME));
    taskRepository.save(expected1);
    taskRepository.save(expected2);

    var tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (var actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(dataEquals(expected1, actual) || dataEquals(expected2, actual)).isTrue();
    }

    var expected3 = TestUtils.createTask3(generateSequence(Task.SEQUENCE_NAME));
    var task = tasks.getContent().get(0);
    task.setDescription(expected3.getDescription());
    task.setStatus(expected3.getStatus());
    task.setType(expected3.getType());
    task.getResult().setValue(expected3.getResult().getValue());
    taskRepository.save(task);

    var optional = taskRepository.findById(task.getId());
    assertThat(optional).isPresent();
    task = optional.get();
    assertThat(dataEquals(expected3, task)).isTrue();

    for (var actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }

  private boolean dataEquals(Task expected, Task actual) {
    return Objects.equals(expected.getDescription(), actual.getDescription())
        && Objects.equals(expected.getStatus(), actual.getStatus())
        && Objects.equals(expected.getType(), actual.getType())
        && Objects.equals(expected.getResult(), actual.getResult());
  }
}
