package it.francescofiora.tasks.taskapi.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.domain.DatabaseSequence;
import it.francescofiora.tasks.taskapi.domain.DomainIdentifier;
import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.domain.Result;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create Parameter.
   *
   * @return Parameter
   */
  public static Object createParameter(String name) {
    var parameter = new Parameter();
    parameter.setName(name);
    return parameter;
  }

  /**
   * Create an example of Parameter.
   *
   * @param name the name of Parameter
   * @param value the value of Parameter
   * @return Parameter
   */
  public static Parameter createParameter(final String name, final String value) {
    var parameter = new Parameter();
    parameter.setName(name);
    parameter.setValue(value);
    return parameter;
  }

  /**
   * create NewTaskDto.
   *
   * @return NewTaskDto
   */
  public static NewTaskDto createNewTaskDto() {
    var taskDto = new NewTaskDto();
    taskDto.setDescription("Description");
    taskDto.setType(TaskType.LONG);
    taskDto.getParameters().add(createParameterDto());
    return taskDto;
  }

  /**
   * create ParameterDto.
   *
   * @return ParameterDto
   */
  public static ParameterDto createParameterDto() {
    var parameterDto = new ParameterDto();
    parameterDto.setName("Name");
    parameterDto.setValue("Value");
    return parameterDto;
  }

  /**
   * create UpdatableTaskDto.
   *
   * @return UpdatableTaskDto
   */
  public static UpdatableTaskDto createUpdatableTaskDto(Long id) {
    var taskDto = new UpdatableTaskDto();
    taskDto.setId(id);
    taskDto.setDescription("Description updated");
    return taskDto;
  }

  /**
   * Create DatabaseSequence.
   *
   * @param id Id
   * @return DatabaseSequence
   */
  public static Object createDatabaseSequence(String id) {
    var dbSequence = new DatabaseSequence();
    dbSequence.setId(id);
    return dbSequence;
  }

  /**
   * Create Result.
   *
   * @param value Value
   * @return Result
   */
  public static Object createResult(String value) {
    var result = new Result();
    result.setValue(value);
    return result;
  }

  /**
   * Create first example of Task.
   *
   * @param id ID
   * @return Task
   */
  public static Task createTask1(final Long id) {
    var task = new Task();
    task.setId(id);
    task.setDescription("first");
    task.setStatus(TaskStatus.SCHEDULATED);
    task.setType(TaskType.SHORT);
    task.setResult(new Result("result 1"));
    task.getParameters().add(createParameter("name", "value"));
    return task;
  }

  /**
   * Create second example of Task.
   *
   * @param id ID
   * @return Task
   */
  public static Task createTask2(final Long id) {
    var task = new Task();
    task.setId(id);
    task.setDescription("second");
    task.setStatus(TaskStatus.SCHEDULATED);
    task.setType(TaskType.LONG);
    task.setResult(new Result("result 2"));
    task.getParameters().add(createParameter("key", "value"));
    return task;
  }

  /**
   * Create third example of Task.
   *
   * @param id ID
   * @return Task
   */
  public static Task createTask3(final Long id) {
    var task = new Task();
    task.setId(id);
    task.setDescription("third");
    task.setStatus(TaskStatus.SCHEDULATED);
    task.setType(TaskType.NEW_TYPE);
    task.setResult(new Result("result 3"));
    task.getParameters().add(createParameter("par", "value"));
    return task;
  }

  /**
   * create MessageDtoRequest.
   *
   * @return MessageDtoRequest
   */
  public static MessageDtoRequest createMessageDtoRequest() {
    // @formatter:off
    return new MessageDtoRequestImpl()
        .taskId(1L)
        .type(TaskType.LONG)
        .addParameter("Key", "Value");
    // @formatter:on
  }

  /**
   * create MessageDtoResponse.
   *
   * @return MessageDtoResponse
   */
  public static MessageDtoResponse createMessageDtoResponse() {
    // @formatter:off
    return new MessageDtoResponseImpl()
        .taskId(1L)
        .type(TaskType.LONG)
        .status(TaskStatus.TERMINATED)
        .result("Result");
    // @formatter:on
  }

  /**
   * create new DomainIdentifier.
   *
   * @param clazz the DomainIdentifier class.
   * @param id the id
   * @return a new DomainIdentifier Object
   * @throws Exception if error occurs
   */
  public static <T> Object createNewDomain(Class<T> clazz, Long id) throws Exception {
    var domainObj = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj.setId(id);
    return domainObj;
  }

  /**
   * assert that obj1 is equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isTrue();
    assertThat(obj1.hashCode()).isEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isEqualTo(obj2.toString());
  }

  /**
   * assert that obj1 is not equal to obj2 and also their hashCode and ToString.
   *
   * @param obj1 the Object to compare
   * @param obj2 the Object to compare
   */
  public static void checkNotEqualHashAndToString(final Object obj1, final Object obj2) {
    assertThat(obj1.equals(obj2)).isFalse();
    assertThat(obj1.hashCode()).isNotEqualTo(obj2.hashCode());
    assertThat(obj1.toString()).isNotEqualTo(obj2.toString());
  }
}
