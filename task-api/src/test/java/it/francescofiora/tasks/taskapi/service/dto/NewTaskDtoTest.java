package it.francescofiora.tasks.taskapi.service.dto;

import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class NewTaskDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewTaskDto newTaskDto1 = new NewTaskDto();
    newTaskDto1.setDescription("Description");
    NewTaskDto newTaskDto2 = new NewTaskDto();
    TestUtils.checkNotEqualHashAndToString(newTaskDto1, newTaskDto2);

    newTaskDto2.setDescription(newTaskDto1.getDescription());
    TestUtils.checkEqualHashAndToString(newTaskDto1, newTaskDto2);

    newTaskDto2.setDescription("Description2");
    TestUtils.checkNotEqualHashAndToString(newTaskDto1, newTaskDto2);

    newTaskDto1.setDescription(null);
    TestUtils.checkNotEqualHashAndToString(newTaskDto1, newTaskDto2);
  }
}
