package it.francescofiora.tasks.taskexecutor.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
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
@WebMvcTest(controllers = TaskExecutorApi.class)
public class TaskExecutorApiTest {
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
  public void testGetAllTasks() throws Exception {
    Pageable pageable = PageRequest.of(1, 1);
    TaskExecutorDto expected = new TaskExecutorDto();
    expected.setId(ID);
    given(taskService.findAll(any(Pageable.class)))
        .willReturn(new PageImpl<TaskExecutorDto>(Collections.singletonList(expected)));

    MvcResult result = mvc.perform(get(new URI(TASKS_URI)).contentType(APPLICATION_JSON)
        .content(mapper.writeValueAsString(pageable))).andExpect(status().isOk()).andReturn();
    List<TaskExecutorDto> list = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<List<TaskExecutorDto>>() {
        });
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  public void testGetTask() throws Exception {
    TaskExecutorDto expected = new TaskExecutorDto();
    expected.setId(ID);
    given(taskService.findOne(eq(ID))).willReturn(Optional.of(expected));
    MvcResult result = mvc.perform(get(TASKS_ID_URI, ID)).andExpect(status().isOk()).andReturn();
    TaskExecutorDto actual = mapper.readValue(result.getResponse().getContentAsString(),
        new TypeReference<TaskExecutorDto>() {
        });
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
