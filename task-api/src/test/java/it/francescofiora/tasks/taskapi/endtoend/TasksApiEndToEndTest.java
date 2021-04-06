package it.francescofiora.tasks.taskapi.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class TasksApiEndToEndTest extends AbstractTestEndToEnd {

  private static final String TASKS_URI = "/api/tasks";
  private static final String TASKS_ID_URI = "/api/tasks/%d";

  private static final String ALERT_CREATED = "TaskDto.created";
  private static final String ALERT_DELETED = "TaskDto.deleted";
  private static final String ALERT_GET = "TaskDto.get";
  private static final String ALERT_BAD_REQUEST = "TaskDto.badRequest";
  private static final String ALERT_NOT_FOUND = "TaskDto.notFound";

  @Test
  public void testAuth() throws Exception {
    testUnauthorized(TASKS_URI);
  }

  @Test
  public void testCreateTask() throws Exception {
    NewTaskDto newTaskDto = TestUtils.createNewTaskDto();
    Long taskId = createAndReturnId(TASKS_URI, newTaskDto, ALERT_CREATED);

    final String tasksIdUri = String.format(TASKS_ID_URI, taskId);

    TaskDto taskDto = get(tasksIdUri, TaskDto.class, ALERT_GET);
    assertThat(taskDto.getDescription()).isEqualTo(taskDto.getDescription());
    assertThat(taskDto.getId()).isEqualTo(taskId);
    assertThat(taskDto.getType()).isEqualTo(newTaskDto.getType());

    TaskDto[] taskDtoArr = get(TASKS_URI, PageRequest.of(1, 1), TaskDto[].class, ALERT_GET);
    assertThat(taskDtoArr).isNotEmpty();
    Optional<TaskDto> option =
        Stream.of(taskDtoArr).filter(task -> task.getId().equals(taskId)).findAny();
    assertThat(option).isPresent();
    assertThat(option.get()).isEqualTo(taskDto);

    delete(tasksIdUri, ALERT_DELETED);

    assertGetNotFound(tasksIdUri, TaskDto.class, ALERT_NOT_FOUND);
  }


  @Test
  public void testGetTaskBadRequest() throws Exception {
    assertGetBadRequest(TASKS_URI + "/999999999999999999999999", String.class, "id.badRequest");
  }

}
