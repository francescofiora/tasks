package it.francescofiora.tasks.taskexecutor.util;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.DomainIdentifier;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.service.dto.ParameterDto;
import it.francescofiora.tasks.util.DtoIdentifier;
import java.sql.Timestamp;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility for testing.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestUtils {

  /**
   * Create MessageDtoRequest.
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
   * Create MessageDtoRequest with NEW_TYPE.
   *
   * @return MessageDtoRequest
   */
  public static MessageDtoRequest createMessageDtoRequestNewType() {
    // @formatter:off
    return new MessageDtoRequestImpl()
        .taskId(1L)
        .type(TaskType.NEW_TYPE)
        .addParameter("Key", "Value");
    // @formatter:on
  }

  /**
   * Create MessageDtoResponse.
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
   * Create Short Task 1.
   *
   * @return Task
   */
  public static Task createShortTask1() {
    // @formatter:off
    return new Task()
        .jmsMessageId("ABC")
        .jobInstanceId(1L)
        .jobName(JobType.SHORT)
        .status(TaskStatus.TERMINATED)
        .taskRef(1L)
        .taskType(TaskType.SHORT.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result1");
    // @formatter:on
  }

  /**
   * Create Short Task 2.
   *
   * @return Task
   */
  public static Task createShortTask2() {
    // @formatter:off
    return new Task()
        .jmsMessageId("FBC")
        .jobInstanceId(2L)
        .jobName(JobType.SHORT)
        .status(TaskStatus.IN_PROGRESS)
        .taskRef(2L)
        .taskType(TaskType.SHORT.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result2");
    // @formatter:on
  }

  /**
   * Create Long Task.
   *
   * @return Task
   */
  public static Task createLongTask() {
    // @formatter:off
    return new Task()
        .jmsMessageId("FWC")
        .jobInstanceId(3L)
        .jobName(JobType.LONG)
        .status(TaskStatus.ERROR)
        .taskRef(3L)
        .taskType(TaskType.LONG.name())
        .messageCreated(new Timestamp(1000L))
        .result("Result3");
    // @formatter:on
  }

  /**
   * Compare if expected Task and actual Task have same data.
   *
   * @param expected Task
   * @param actual Task
   * @return true if data are equal
   */
  public static boolean taskEquals(Task expected, Task actual) {
    return expected.getJmsMessageId().equals(actual.getJmsMessageId())
        && expected.getJobInstanceId().equals(actual.getJobInstanceId())
        && expected.getJobName().equals(actual.getJobName())
        && expected.getMessageCreated().equals(actual.getMessageCreated())
        && expected.getResult().equals(actual.getResult())
        && expected.getStatus().equals(actual.getStatus())
        && expected.getTaskRef().equals(actual.getTaskRef())
        && expected.getTaskType().equals(actual.getTaskType());
  }

  /**
   * Create ParameterDto.
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
   * Create new DomainIdentifier.
   *
   * @param clazz the DomainIdentifier class.
   * @param id the id
   * @return a new DomainIdentifier
   * @throws Exception if error occurs
   */
  public static <T> DomainIdentifier createNewDomain(Class<T> clazz, Long id) throws Exception {
    var domainObj = (DomainIdentifier) clazz.getConstructor().newInstance();
    domainObj.setId(id);
    return domainObj;
  }

  /**
   * Create new DtoIdentifier.
   *
   * @param clazz the DtoIdentifier class.
   * @param id the id
   * @return a new DtoIdentifier
   * @throws Exception if error occurs
   */
  public static <T> DtoIdentifier createNewDtoIdentifier(Class<T> clazz, Long id) throws Exception {
    var dtoObj = (DtoIdentifier) clazz.getConstructor().newInstance();
    dtoObj.setId(id);
    return dtoObj;
  }

  /**
   * Assert that obj1 is equal to obj2 and also their hashCode and ToString.
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
   * Assert that obj1 is not equal to obj2 and also their hashCode and ToString.
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
