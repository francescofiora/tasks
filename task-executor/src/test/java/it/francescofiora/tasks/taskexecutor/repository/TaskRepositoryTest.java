package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import java.sql.Timestamp;
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
    Task expected1 = createLongTask();
    Task expected2 = createShortTask2();
    taskRepository.save(expected1);
    taskRepository.save(expected2);

    Page<Task> tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isNotEmpty();

    for (Task actual : tasks) {
      assertThat(actual).isNotNull();
      assertThat(assertEquals(expected1, actual)
          || assertEquals(expected2, actual)).isTrue();
    }

    Task expected3 = createShortTask1();
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
    assertThat(assertEquals(expected3, task)).isTrue();

    for (Task actual : tasks) {
      taskRepository.delete(actual);
    }

    tasks = taskRepository.findAll(PageRequest.of(0, 10));
    assertThat(tasks).isNotNull().isEmpty();
  }

  private boolean assertEquals(Task expected, Task actual) {
    return expected.getJmsMessageId().equals(actual.getJmsMessageId())
        && expected.getJobInstanceId().equals(actual.getJobInstanceId())
        && expected.getJobName().equals(actual.getJobName())
        && expected.getMessageCreated().equals(actual.getMessageCreated())
        && expected.getResult().equals(actual.getResult())
        && expected.getStatus().equals(actual.getStatus())
        && expected.getTaskRef().equals(actual.getTaskRef())
        && expected.getTaskType().equals(actual.getTaskType());
  }

  private Task createShortTask1() {
    return new Task()
        .jmsMessageId("ABC")
        .jobInstanceId(1L)
        .jobName(JobType.SHORT)
        .status(TaskStatus.TERMINATED)
        .taskRef(1L)
        .taskType(TaskType.SHORT.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result1");
  }

  private Task createShortTask2() {
    return new Task()
        .jmsMessageId("FBC")
        .jobInstanceId(2L)
        .jobName(JobType.SHORT)
        .status(TaskStatus.IN_PROGRESS)
        .taskRef(2L)
        .taskType(TaskType.SHORT.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result2");
  }

  private Task createLongTask() {
    return new Task()
        .jmsMessageId("FWC")
        .jobInstanceId(3L)
        .jobName(JobType.LONG)
        .status(TaskStatus.ERROR)
        .taskRef(3L)
        .taskType(TaskType.LONG.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result3");
  }
  
}
