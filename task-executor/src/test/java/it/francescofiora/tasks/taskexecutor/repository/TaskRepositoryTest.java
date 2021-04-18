package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public class TaskRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void testCrud() throws Exception {
    Task expected1 = TestUtils.createLongTask();
    Task expected2 = TestUtils.createShortTask2();
    taskRepository.save(expected1);
    taskRepository.save(expected2);

    Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (Task actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(TestUtils.taskEquals(expected1, actual) || TestUtils.taskEquals(expected2, actual))
          .isTrue();
    }

    Task expected3 = TestUtils.createShortTask1();
    Task task = tasks.getContent().get(0);
    task.setJmsMessageId(expected3.getJmsMessageId());
    task.setJobInstanceId(expected3.getJobInstanceId());
    task.setJobName(expected3.getJobName());
    task.setMessageCreated(expected3.getMessageCreated());
    task.setResult(expected3.getResult());
    task.setStatus(expected3.getStatus());
    task.setTaskRef(expected3.getTaskRef());
    task.setTaskType(expected3.getTaskType());
    taskRepository.save(task);

    Optional<Task> optional = taskRepository.findById(task.getId());
    assertThat(optional).isPresent();
    task = optional.get();
    assertThat(TestUtils.taskEquals(expected3, task)).isTrue();

    for (Task actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }
}
