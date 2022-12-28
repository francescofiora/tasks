package it.francescofiora.tasks.taskexecutor.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import(ProjectInfoAutoConfiguration.class)
@WebMvcTest(controllers = TaskExecutorApi.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class TaskExecutorApiTest extends AbstractTestApi {
  private static final Long ID = 1L;
  private static final String TASKS_URI = "/api/v1/tasks";
  private static final String TASKS_ID_URI = "/api/v1/tasks/{id}";
  private static final String WRONG_URI = "/api/v1/wrong";

  @MockBean
  private TaskService taskService;

  @Test
  void testGetAllTasks() throws Exception {
    var pageable = PageRequest.of(1, 1);
    var expected = new TaskExecutorDto();
    expected.setId(ID);
    given(taskService.findAll(any(), any(), any(), any(), any(Pageable.class)))
        .willReturn(new PageImpl<TaskExecutorDto>(List.of(expected)));

    var result = performGet(TASKS_URI, pageable).andExpect(status().isOk()).andReturn();
    var list = readValue(result, new TypeReference<List<TaskExecutorDto>>() {});
    assertThat(list).isNotNull().isNotEmpty();
    assertThat(list.get(0)).isEqualTo(expected);
  }

  @Test
  void testGetTask() throws Exception {
    var expected = new TaskExecutorDto();
    expected.setId(ID);
    given(taskService.findOne(eq(ID))).willReturn(Optional.of(expected));
    var result = performGet(TASKS_ID_URI, ID).andExpect(status().isOk()).andReturn();
    var actual = readValue(result, new TypeReference<TaskExecutorDto>() {});
    assertThat(actual).isNotNull().isEqualTo(expected);
  }

  @Test
  void testDeleteTask() throws Exception {
    performDelete(TASKS_ID_URI, ID).andExpect(status().isNoContent()).andReturn();
  }

  @Test
  void testWrongUri() throws Exception {
    performGet(WRONG_URI).andExpect(status().isNotFound()).andReturn();
  }
}
