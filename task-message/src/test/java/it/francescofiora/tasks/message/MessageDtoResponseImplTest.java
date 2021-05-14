package it.francescofiora.tasks.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.util.DtoEqualsTester;
import it.francescofiora.tasks.util.TestUtils;
import org.junit.jupiter.api.Test;

class MessageDtoResponseImplTest {

  private static final Long ID = 1L;
  private static final TaskType TASK_TYPE_LONG = TaskType.LONG;
  private static final String RESULT = "Result";
  private static final TaskStatus STATUS_TERMINATED = TaskStatus.TERMINATED;

  @Test
  void testDtoStructureAndBehavior() {
    // @formatter:off
    Validator validator = ValidatorBuilder
        .create()
        .with(new GetterMustExistRule())
        .with(new SetterMustExistRule())
        .with(new GetterTester())
        .with(new SetterTester())
        .with(new DtoEqualsTester())
        .build();
    // @formatter:on

    validator.validate(PojoClassFactory.getPojoClass(MessageDtoResponseImpl.class));
  }

  @Test
  void testBuilder() {
    MessageDtoResponseImpl response1 = new MessageDtoResponseImpl();
    response1.setTaskId(ID);
    response1.setType(TASK_TYPE_LONG);
    response1.setResult(RESULT);
    response1.setStatus(STATUS_TERMINATED);

    MessageDtoResponseImpl response2 = buildResponse(ID, TASK_TYPE_LONG, RESULT, STATUS_TERMINATED);

    assertThat(response2.getTaskId()).isEqualTo(response1.getTaskId());
    assertThat(response2.getType()).isEqualTo(response1.getType());
    assertThat(response2.getResult()).isEqualTo(response1.getResult());
    assertThat(response2.getStatus()).isEqualTo(response1.getStatus());
  }

  private MessageDtoResponseImpl buildResponse(Long taskId, TaskType type, String result,
      TaskStatus status) {
    // @formatter:off
    return new MessageDtoResponseImpl()
        .taskId(taskId)
        .type(type)
        .result(result)
        .status(status);
    // @formatter:on
  }

  @Test
  void equalsVerifier() throws Exception {
    MessageDtoResponseImpl response1 = buildResponse(ID, TASK_TYPE_LONG, RESULT, STATUS_TERMINATED);
    MessageDtoResponseImpl response2 = buildResponse(ID, TASK_TYPE_LONG, RESULT, STATUS_TERMINATED);
    TestUtils.checkEqualHashAndToString(response1, response2);

    response2.setTaskId(2L);
    TestUtils.checkNotEqualHashAndToString(response1, response2);

    response1.setTaskId(null);
    TestUtils.checkNotEqualHashAndToString(response1, response2);
  }
}
