package it.francescofiora.tasks.taskapi.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.jms.JmsConsumer;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import java.util.stream.Stream;
import org.apache.activemq.command.ActiveMQTextMessage;
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
class TasksApiEndToEndTest extends AbstractTestEndToEnd {

  private static final String TASKS_URI = "/tasks-api/api/v1/tasks";
  private static final String TASKS_ID_URI = "/tasks-api/api/v1/tasks/%d";

  private static final String ALERT_CREATED = "TaskDto.created";
  private static final String ALERT_DELETED = "TaskDto.deleted";
  private static final String ALERT_PATCHED = "TaskDto.patched";
  private static final String ALERT_GET = "TaskDto.get";
  private static final String ALERT_BAD_REQUEST = "NewTaskDto.badRequest";
  private static final String ALERT_PATCH_BAD_REQUEST = "TaskDto.badRequest";
  private static final String ALERT_NOT_FOUND = "TaskDto.notFound";

  private static final String PARAM_PAGE_20 = "0 20";
  private static final String PARAM_NOT_VALID_LONG =
      "'id' should be a valid 'Long' and '999999999999999999999999' isn't";

  private static final String PARAM_DESCRIPTION_NOT_BLANK = "[newTaskDto.description - NotBlank]";
  private static final String PARAM_PARAMETERS_NOT_EMPTY = "[newTaskDto.parameters - NotEmpty]";
  private static final String PARAM_VALUE_NOT_EMPTY = "[newTaskDto.parameters[].value - NotBlank]";
  private static final String PARAM_NAME_NOT_EMPTY = "[newTaskDto.parameters[].name - NotBlank]";
  private static final String PARAM_TYPE_NOT_EMPTY = "[newTaskDto.type - NotNull]";

  @Autowired
  private JmsConsumer jmsConsumer;

  @Autowired
  private ObjectMapper mapper;

  @Test
  void testAuth() throws Exception {
    testUnauthorized(TASKS_URI);
  }

  @Test
  void testCreateTask() throws Exception {
    var newTaskDto = TestUtils.createNewTaskDto();
    var id = createAndReturnId(TASKS_URI, newTaskDto, ALERT_CREATED);

    final var tasksIdUri = String.format(TASKS_ID_URI, id);

    var taskDto = get(tasksIdUri, TaskDto.class, ALERT_GET, String.valueOf(id));
    assertThat(taskDto.getDescription()).isEqualTo(newTaskDto.getDescription());
    assertThat(taskDto.getId()).isEqualTo(id);
    assertThat(taskDto.getType()).isEqualTo(newTaskDto.getType());
    assertThat(taskDto.getParameters()).hasSize(newTaskDto.getParameters().size());
    assertThat(taskDto.getParameters()).isEqualTo(newTaskDto.getParameters());

    var taskDtoArr =
        get(TASKS_URI, PageRequest.of(1, 1), TaskDto[].class, ALERT_GET, PARAM_PAGE_20);
    assertThat(taskDtoArr).isNotEmpty();
    var option = Stream.of(taskDtoArr).filter(task -> task.getId().equals(id)).findAny();
    assertThat(option).isPresent().contains(taskDto);

    var updatabletaskDto = TestUtils.createUpdatableTaskDto(id);
    patch(tasksIdUri, updatabletaskDto, ALERT_PATCHED, String.valueOf(id));

    taskDto = get(tasksIdUri, TaskDto.class, ALERT_GET, String.valueOf(id));
    assertThat(taskDto.getDescription()).isEqualTo(updatabletaskDto.getDescription());

    delete(tasksIdUri, ALERT_DELETED, String.valueOf(id));

    assertGetNotFound(tasksIdUri, TaskDto.class, ALERT_NOT_FOUND, String.valueOf(id));
  }

  @Test
  void testBadRequest() throws Exception {
    var newTaskDto = TestUtils.createNewTaskDto();

    // description
    newTaskDto.setDescription(null);
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_DESCRIPTION_NOT_BLANK);

    newTaskDto.setDescription("");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_DESCRIPTION_NOT_BLANK);

    newTaskDto.setDescription("  ");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_DESCRIPTION_NOT_BLANK);

    // type
    newTaskDto = TestUtils.createNewTaskDto();
    newTaskDto.setType(null);
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_TYPE_NOT_EMPTY);

    // parameters
    newTaskDto = TestUtils.createNewTaskDto();
    newTaskDto.setParameters(null);
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_PARAMETERS_NOT_EMPTY);

    newTaskDto = TestUtils.createNewTaskDto();
    newTaskDto.getParameters().clear();
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_PARAMETERS_NOT_EMPTY);

    // value
    newTaskDto = TestUtils.createNewTaskDto();
    var param = new ParameterDto();
    param.setName("NewName");
    newTaskDto.getParameters().add(param);
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_VALUE_NOT_EMPTY);

    param.setValue("");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_VALUE_NOT_EMPTY);

    param.setValue("  ");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_VALUE_NOT_EMPTY);

    // name
    param.setValue("Value");
    param.setName(null);
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_EMPTY);

    param.setName("");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_EMPTY);

    param.setName("  ");
    assertCreateBadRequest(TASKS_URI, newTaskDto, ALERT_BAD_REQUEST, PARAM_NAME_NOT_EMPTY);

    final var tasksIdUri = String.format(TASKS_ID_URI, 1L);
    var updatabletaskDto = TestUtils.createUpdatableTaskDto(2L);
    assertPatchBadRequest(tasksIdUri, updatabletaskDto, ALERT_PATCH_BAD_REQUEST, "2");
  }

  @Test
  void testGetTaskBadRequest() throws Exception {
    assertGetBadRequest(TASKS_URI + "/999999999999999999999999", String.class, "id.badRequest",
        PARAM_NOT_VALID_LONG);
  }

  @Test
  void testResponse() throws Exception {
    var newTaskDto = TestUtils.createNewTaskDto();
    var id = createAndReturnId(TASKS_URI, newTaskDto, ALERT_CREATED);

    var message = new ActiveMQTextMessage();
    var response = new MessageDtoResponseImpl().taskId(id).type(TaskType.LONG)
        .status(TaskStatus.TERMINATED).result("Result");
    message.setText(mapper.writeValueAsString(response));
    jmsConsumer.receiveMessage(message);

    final var tasksIdUri = String.format(TASKS_ID_URI, id);
    var taskDto = get(tasksIdUri, TaskDto.class, ALERT_GET, String.valueOf(id));
    assertThat(taskDto.getResult().getValue()).isEqualTo(response.getResult());
    assertThat(taskDto.getStatus()).isEqualTo(TaskStatus.TERMINATED);
  }
}
