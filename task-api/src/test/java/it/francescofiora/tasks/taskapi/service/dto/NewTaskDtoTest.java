package it.francescofiora.tasks.taskapi.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class NewTaskDtoTest {

  @Test
  public void dtoEqualsVerifier() throws Exception {
    NewTaskDto newTaskDto1 = new NewTaskDto();
    newTaskDto1.setDescription("Description");
    NewTaskDto newTaskDto2 = new NewTaskDto();
    assertThat(newTaskDto1).isNotEqualTo(newTaskDto2);
    newTaskDto2.setDescription(newTaskDto1.getDescription());
    assertThat(newTaskDto1).isEqualTo(newTaskDto2);
    newTaskDto2.setDescription("Description2");
    assertThat(newTaskDto1).isNotEqualTo(newTaskDto2);
    newTaskDto1.setDescription(null);
    assertThat(newTaskDto1).isNotEqualTo(newTaskDto2);
  }
}
