package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;

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
    task.setJmsMessageId(expecteted3.getJmsMessageId());
    task.setJobInstanceId(expecteted3.getJobInstanceId());
    task.setJobName(expecteted3.getJobName());
    task.setMessageCreated(expecteted3.getMessageCreated());
    task.setResult(expecteted3.getResult());
    task.setStatus(expecteted3.getStatus());
    task.setTaskRef(expecteted3.getTaskRef());
    task.setTaskType(expecteted3.getTaskType());
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

  private boolean assertEquals(Task expecteted, Task actual) {
    return expecteted.getJmsMessageId().equals(actual.getJmsMessageId())
        && expecteted.getJobInstanceId().equals(actual.getJobInstanceId())
        && expecteted.getJobName().equals(actual.getJobName())
        && expecteted.getMessageCreated().equals(actual.getMessageCreated())
        && expecteted.getResult().equals(actual.getResult())
        && expecteted.getStatus().equals(actual.getStatus())
        && expecteted.getTaskRef().equals(actual.getTaskRef())
        && expecteted.getTaskType().equals(actual.getTaskType());
  }

  private Task createTask3() {
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

  private Task createTask2() {
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

  private Task createTask1() {
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
