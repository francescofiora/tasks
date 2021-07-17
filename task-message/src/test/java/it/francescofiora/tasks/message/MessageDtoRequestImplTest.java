package it.francescofiora.tasks.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.util.DtoEqualsTester;
import it.francescofiora.tasks.util.TestUtils;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MessageDtoRequestImplTest {

  private static final String KEY = "Key";
  private static final String VALUE = "Value";

  private static final Long ID = 1L;
  private static final TaskType TASK_TYPE = TaskType.LONG;

  private static final String KEY2 = "SecondKey";
  private static final String VALUE2 = "SecondValue";

  @Test
  void testDtoStructureAndBehavior() {
    // @formatter:off
    var validator = ValidatorBuilder
        .create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new GetterTester())
        .with(new SetterTester())
        .with(new DtoEqualsTester())
        .build();
    // @formatter:on

    validator.validate(PojoClassFactory.getPojoClass(MessageDtoRequestImpl.class));
  }

  @Test
  void testBuilder() {
    var request2 = buildRequest(ID, TASK_TYPE, KEY, VALUE);

    assertThat(request2.getTaskId()).isEqualTo(ID);
    assertThat(request2.getType()).isEqualTo(TASK_TYPE);
    assertThat(request2.getParameters()).hasSize(1).containsEntry(KEY, VALUE);
  }

  private MessageDtoRequestImpl buildRequest(Long taskId, TaskType type, String parKey,
      String parValue) {
    // @formatter:off
    return new MessageDtoRequestImpl()
        .taskId(taskId)
        .type(type)
        .addParameter(parKey, parValue);
    // @formatter:on
  }

  @Test
  void equalsVerifier() throws Exception {
    var request1 = buildRequest(ID, TASK_TYPE, KEY, VALUE);
    var request2 = buildRequest(ID, TASK_TYPE, KEY, VALUE);
    TestUtils.checkEqualHashAndToString(request1, request2);

    request2.setTaskId(2L);
    TestUtils.checkNotEqualHashAndToString(request1, request2);

    request1.setTaskId(null);
    TestUtils.checkNotEqualHashAndToString(request1, request2);
  }

  @Test
  void addParameters() throws Exception {
    // @formatter:off
    var request = new MessageDtoRequestImpl()
        .addParameters(null)
        .addParameters(Map.of(KEY, VALUE))
        .addParameters(Collections.emptyMap())
        .addParameters(Map.of(KEY2, VALUE2));
    // @formatter:on

    // @formatter:off
    assertThat(request.getParameters())
      .hasSize(2)
      .containsEntry(KEY, VALUE)
      .containsEntry(KEY2, VALUE2);
    // @formatter:on
  }
}
