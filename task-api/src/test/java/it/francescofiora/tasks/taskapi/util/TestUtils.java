package it.francescofiora.tasks.taskapi.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.domain.Parameter;
import it.francescofiora.tasks.taskapi.domain.Result;
import it.francescofiora.tasks.taskapi.domain.Task;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.ParameterDto;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtils {

  private static final ObjectMapper mapper = createObjectMapper();

  private static ObjectMapper createObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert
   * @return the JSON byte array
   * @throws IOException if occurred
   */
  public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
    return mapper.writeValueAsBytes(object);
  }

  /**
   * Create a byte array with a specific size filled with specified data.
   *
   * @param size the size of the byte array
   * @param data the data to put in the byte array
   * @return the JSON byte array
   */
  public static byte[] createByteArray(int size, String data) {
    byte[] byteArray = new byte[size];
    for (int i = 0; i < size; i++) {
      byteArray[i] = Byte.parseByte(data, 2);
    }
    return byteArray;
  }

  /**
   * A matcher that tests that the examined string represents the same instant as the reference
   * datetime.
   */
  public static class ZonedDateTimeMatcher extends TypeSafeDiagnosingMatcher<String> {

    private final ZonedDateTime date;

    public ZonedDateTimeMatcher(ZonedDateTime date) {
      this.date = date;
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
      try {
        if (!date.isEqual(ZonedDateTime.parse(item))) {
          mismatchDescription.appendText("was ").appendValue(item);
          return false;
        }
        return true;
      } catch (DateTimeParseException e) {
        mismatchDescription.appendText("was ").appendValue(item)
            .appendText(", which could not be parsed as a ZonedDateTime");
        return false;
      }

    }

    @Override
    public void describeTo(Description description) {
      description.appendText("a String representing the same Instant as ").appendValue(date);
    }
  }

  /**
   * Creates a matcher that matches when the examined string represents the same instant as the
   * reference datetime.
   *
   * @param date the reference datetime against which the examined string is checked
   */
  public static ZonedDateTimeMatcher sameInstant(ZonedDateTime date) {
    return new ZonedDateTimeMatcher(date);
  }

  /**
   * Create a {@link FormattingConversionService} which use ISO date format, instead of the
   * localized one.
   *
   * @return the {@link FormattingConversionService}
   */
  public static FormattingConversionService createFormattingConversionService() {
    DefaultFormattingConversionService dfcs = new DefaultFormattingConversionService();
    DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
    registrar.setUseIsoFormat(true);
    registrar.registerFormatters(dfcs);
    return dfcs;
  }

  /**
   * Create an example of Parameter.
   *
   * @param name the name of Parameter
   * @param value the value of Parameter
   * @return Parameter
   */
  public static Parameter createParameter(final String name, final String value) {
    Parameter parameter = new Parameter();
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
    NewTaskDto taskDto = new NewTaskDto();
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
    ParameterDto parameterDto = new ParameterDto();
    parameterDto.setName("Name");
    parameterDto.setValue("Value");
    return parameterDto;
  }

  /**
   * Create first example of Task.
   *
   * @param id ID
   * @return Task
   */
  public static Task createTask1(final Long id) {
    Task task = new Task();
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
    Task task = new Task();
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
    Task task = new Task();
    task.setId(id);
    task.setDescription("third");
    task.setStatus(TaskStatus.SCHEDULATED);
    task.setType(TaskType.NEW_TYPE);
    task.setResult(new Result("result 3"));
    task.getParameters().add(createParameter("par", "value"));
    return task;
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

  private TestUtils() {}
}
