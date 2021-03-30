package it.francescofiora.tasks.taskapi.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.service.TaskService;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TasksApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
public class TasksApiTest extends AbstractTestApi {

  private static final Long ID = 1L;
  private static final String TASKS_URI = "/api/tasks";
  private static final String TASKS_ID_URI = "/api/tasks/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @MockBean
  private TaskService taskService;

  @Test
  public void testCreateTask() throws Exception {
    NewTaskDto newTaskDto = getNewTaskDto();

    TaskDto taskDto = new TaskDto();
    taskDto.setId(ID);
    given(taskService.create(any(NewTaskDto.class))).willReturn(taskDto);
    MvcResult result =
        performPost(TASKS_URI, newTaskDto).andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(TASKS_URI + "/" + ID);
  }

  @Test
  public void testCreateTaskBadRequest() throws Exception {
    // description
    NewTaskDto newTaskDto = getNewTaskDto();
    newTaskDto.setDescription(null);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.setDescription("");
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.setDescription("  ");
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    // type
    newTaskDto = getNewTaskDto();
    newTaskDto.setType(null);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    // parameters
    newTaskDto = getNewTaskDto();
    newTaskDto.setParameters(null);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.getParameters().clear();
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.getParameters().add(new ParameterDto());
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    ParameterDto parameterDto = getParameterDto();
    parameterDto.setName("");
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());


    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setName("  ");
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setName(null);
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue("");
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue("  ");
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue(null);
    newTaskDto.getParameters().add(parameterDto);
    performPost(TASKS_URI, newTaskDto).andExpect(status().isBadRequest());
  }

  @Test
  public void testPatchTaskBadRequest() throws Exception {
    // id
    UpdatableTaskDto taskDto = getUpdatableTaskDto();
    taskDto.setId(null);
    performPatch(TASKS_ID_URI, ID, taskDto).andExpect(status().isBadRequest());

    // description
    taskDto = getUpdatableTaskDto();
    taskDto.setDescription(null);
    performPatch(TASKS_ID_URI, taskDto.getId(), taskDto).andExpect(status().isBadRequest());

    taskDto = getUpdatableTaskDto();
    taskDto.setDescription("");
    performPatch(TASKS_ID_URI, taskDto.getId(), taskDto).andExpect(status().isBadRequest());

    taskDto = getUpdatableTaskDto();
    taskDto.setDescription("  ");
    performPatch(TASKS_ID_URI, taskDto.getId(), taskDto).andExpect(status().isBadRequest());
  }

  @Test
  public void testPatchTask() throws Exception {
    UpdatableTaskDto taskDto = getUpdatableTaskDto();
    performPatch(TASKS_ID_URI, taskDto.getId(), taskDto).andExpect(status().isOk());
  }

  private UpdatableTaskDto getUpdatableTaskDto() {
    UpdatableTaskDto taskDto = new UpdatableTaskDto();
    taskDto.setId(ID);
    taskDto.setDescription("Description II");
    return taskDto;
  }

  private void fillTask(NewTaskDto taskDto) {
    taskDto.setDescription("Description");
    taskDto.setType(TaskType.SHORT);
    ParameterDto parameterDto = new ParameterDto();
    parameterDto.setName("Name");
    parameterDto.setValue("Value");
    taskDto.getParameters().add(parameterDto);
  }

  private NewTaskDto getNewTaskDto() {
    NewTaskDto newTaskDto = new NewTaskDto();
    fillTask(newTaskDto);
    return newTaskDto;
  }

  private ParameterDto getParameterDto() {
    ParameterDto parameterDto = new ParameterDto();
    parameterDto.setName("Paramenter");
    parameterDto.setValue("Par Value");
    return parameterDto;
  }

  @Test
  public void testGetAllTasks() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    TaskDto expected = new TaskDto();
    expected.setId(ID);
    given(taskService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<TaskDto>(Collections.singletonList(expected)));

    MvcResult result = performGet(TASKS_URI, pageable).andExpect(status().isOk()).andReturn();
    List<TaskDto> list = readValue(result, new TypeReference<List<TaskDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetTask() throws Exception {
    TaskDto expected = new TaskDto();
    expected.setId(ID);
    given(taskService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = performGet(TASKS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    TaskDto actual = readValue(result, new TypeReference<TaskDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteTask() throws Exception {
    performDelete(TASKS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
