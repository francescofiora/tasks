package it.francescofiora.tasks.taskapi.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.service.TaskService;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TasksApi.class)
public class TasksApiTest {

  private static final Long ID = 1L;
  private static final String TASKS_URI = "/api/tasks";
  private static final String TASKS_ID_URI = "/api/tasks/{id}";
  private static final String WRONG_URI = "/api/wrong";

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TaskService taskService;

  @Autowired
  private ObjectMapper mapper;

  @Test
  public void testCreateTask() throws Exception {
    NewTaskDto newTaskDto = getNewTaskDto();

    TaskDto taskDto = new TaskDto();
    taskDto.setId(ID);
    given(taskService.create(any(NewTaskDto.class))).willReturn(taskDto);
    MvcResult result = mvc
        .perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
            .content(mapper.writeValueAsString(newTaskDto)))
        .andExpect(status().isCreated()).andReturn();
    assertThat(result.getResponse().getHeaderValue("location")).isEqualTo(TASKS_URI + "/" + ID);
  }

  @Test
  public void testCreateTaskBadRequest() throws Exception {
    // description
    NewTaskDto newTaskDto = getNewTaskDto();
    newTaskDto.setDescription(null);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.setDescription("");
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.setDescription("  ");
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    // type
    newTaskDto = getNewTaskDto();
    newTaskDto.setType(null);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    // parameters
    newTaskDto = getNewTaskDto();
    newTaskDto.setParameters(null);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.getParameters().clear();
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    newTaskDto.getParameters().add(new ParameterDto());
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    ParameterDto parameterDto = getParameterDto();
    parameterDto.setName("");
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());


    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setName("  ");
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setName(null);
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue("");
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue("  ");
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());

    newTaskDto = getNewTaskDto();
    parameterDto = getParameterDto();
    parameterDto.setValue(null);
    newTaskDto.getParameters().add(parameterDto);
    mvc.perform(post(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(newTaskDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testPatchTaskBadRequest() throws Exception {
    // id
    UpdatableTaskDto taskDto = getUpdatableTaskDto();
    taskDto.setId(null);
    mvc.perform(patch(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(taskDto))).andExpect(status().isBadRequest());

    // description
    taskDto = getUpdatableTaskDto();
    taskDto.setDescription(null);
    mvc.perform(patch(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(taskDto))).andExpect(status().isBadRequest());

    taskDto = getUpdatableTaskDto();
    taskDto.setDescription("");
    mvc.perform(patch(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(taskDto))).andExpect(status().isBadRequest());

    taskDto = getUpdatableTaskDto();
    taskDto.setDescription("  ");
    mvc.perform(patch(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(taskDto))).andExpect(status().isBadRequest());
  }

  @Test
  public void testPatchTask() throws Exception {
    UpdatableTaskDto taskDto = getUpdatableTaskDto();
    mvc.perform(patch(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(taskDto))).andExpect(status().isOk());
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

    MvcResult result = mvc.perform(get(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<TaskDto> list = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<TaskDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetTask() throws Exception {
    TaskDto expected = new TaskDto();
    expected.setId(ID);
    given(taskService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(TASKS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    TaskDto actual = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<TaskDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  public void testDeleteTask() throws Exception {
    mvc.perform(delete(TASKS_ID_URI, ID)).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  public void testWrongUri() throws Exception {
    mvc.perform(get(WRONG_URI)).andExpect(status().isNotFound()).andReturn();
  }
}
