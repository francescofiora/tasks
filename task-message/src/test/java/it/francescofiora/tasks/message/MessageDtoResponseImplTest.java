package it.francescofiora.tasks.message;

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
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class MessageDtoResponseImplTest {

  @Test
  public void testDtoStructureAndBehavior() {
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
  public void testBuilder() {
    MessageDtoResponseImpl request1 = new MessageDtoResponseImpl();
    request1.setTaskId(1L);
    request1.setType(TaskType.LONG);
    request1.setResult("Result");
    request1.setStatus(TaskStatus.TERMINATED);

    MessageDtoResponseImpl request2 = buildRequest(request1.getTaskId(), request1.getType(),
        request1.getResult(), request1.getStatus());

    assertThat(request2.getTaskId()).isEqualTo(request1.getTaskId());
    assertThat(request2.getType()).isEqualTo(request1.getType());
    assertThat(request2.getResult()).isEqualTo(request1.getResult());
    assertThat(request2.getStatus()).isEqualTo(request1.getStatus());
  }

  private MessageDtoResponseImpl buildRequest(Long taskId, TaskType type, String result,
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
  public void equalsVerifier() throws Exception {
    MessageDtoResponseImpl request1 =
        buildRequest(1L, TaskType.LONG, "Result", TaskStatus.TERMINATED);

    MessageDtoResponseImpl request2 = new MessageDtoResponseImpl();
    request2.setTaskId(request1.getTaskId());
    assertThat(request1).isEqualTo(request2);

    request2.setTaskId(2L);
    assertThat(request1).isNotEqualTo(request2);

    request1.setTaskId(null);
    assertThat(request1).isNotEqualTo(request2);
  }
}
