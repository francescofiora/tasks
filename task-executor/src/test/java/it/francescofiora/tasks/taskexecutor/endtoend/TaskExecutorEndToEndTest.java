package it.francescofiora.tasks.taskexecutor.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class TaskExecutorEndToEndTest extends AbstractTestEndToEnd {

  private static final String TASKS_URI = "/api/tasks";
  private static final String TASKS_ID_URI = "/api/tasks/%d";

  private static final String ALERT_GET = "TaskExecutorDto.get";
  private static final String ALERT_DELETED = "TaskExecutorDto.deleted";
  private static final String ALERT_NOT_FOUND = "TaskExecutorDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  @Autowired
  private TaskService taskService;

  @Test
  void testAuth() throws Exception {
    testUnauthorized(TASKS_URI);
  }

  @Test
  void testGetTaskBadRequest() throws Exception {
    assertGetBadRequest(TASKS_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }

  @Test
  void testGetTask() throws Exception {
    var task = TestUtils.createLongTask();
    task.setParameters(Collections.singleton(new Parameter().name("name").value("value")));
    task = taskService.save(task);
    final var id = task.getId();

    final var tasksIdUri = String.format(TASKS_ID_URI, id);

    var taskDto = get(tasksIdUri, TaskExecutorDto.class, ALERT_GET, String.valueOf(id));
    assertThat(taskDto.getId()).isEqualTo(id);
    assertThat(taskDto.getJmsMessageId()).isEqualTo(task.getJmsMessageId());
    assertThat(taskDto.getJob().getId()).isEqualTo(task.getJobInstanceId());
    assertThat(taskDto.getJob().getJobName()).isEqualTo(task.getJobName());
    assertThat(taskDto.getResult().getValue()).isEqualTo(task.getResult());
    assertThat(taskDto.getStatus()).isEqualTo(task.getStatus());
    assertThat(taskDto.getMessageCreated()).isEqualTo(task.getMessageCreated());
    assertThat(taskDto.getTask().getId()).isEqualTo(task.getTaskRef());

    var taskDtoArr =
        get(TASKS_URI, PageRequest.of(1, 1), TaskExecutorDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(taskDtoArr).isNotEmpty();
    var option = Stream.of(taskDtoArr).filter(tsk -> tsk.getId().equals(id)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(taskDto);

    delete(tasksIdUri, ALERT_DELETED, String.valueOf(id));

    assertGetNotFound(tasksIdUri, TaskExecutorDto.class, ALERT_NOT_FOUND, String.valueOf(id));
  }
}
