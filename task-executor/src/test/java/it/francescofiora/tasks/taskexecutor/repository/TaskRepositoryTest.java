package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class TaskRepositoryTest extends AbstractTestRepository {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  void testCrud() {
    var expected1 = TestUtils.createLongTask();
    var expected2 = TestUtils.createShortTask2();
    taskRepository.save(expected1);
    taskRepository.save(expected2);

    var tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (var actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(TestUtils.taskEquals(expected1, actual) || TestUtils.taskEquals(expected2, actual))
          .isTrue();
    }

    var expected3 = TestUtils.createShortTask1();
    var task = tasks.getContent().get(0);
    task.setJmsMessageId(expected3.getJmsMessageId());
    task.setJobInstanceId(expected3.getJobInstanceId());
    task.setJobName(expected3.getJobName());
    task.setMessageCreated(expected3.getMessageCreated());
    task.setResult(expected3.getResult());
    task.setStatus(expected3.getStatus());
    task.setTaskRef(expected3.getTaskRef());
    task.setTaskType(expected3.getTaskType());
    taskRepository.save(task);

    var optional = taskRepository.findById(task.getId());
    assertThat(optional).isPresent();
    task = optional.get();
    assertThat(TestUtils.taskEquals(expected3, task)).isTrue();

    for (var actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }
}
