package it.francescofiora.tasks.message;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.util.DtoEqualsTester;

public class MessageDtoRequestImplTest {

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

    validator.validate(PojoClassFactory.getPojoClass(MessageDtoRequestImpl.class));
  }

  @Test
  public void testBuilder() {
    MessageDtoRequestImpl request1 = new MessageDtoRequestImpl();
    request1.setTaskId(1L);
    request1.setType(TaskType.LONG);
    request1.getParameters().put("Key", "Value");

    MessageDtoRequestImpl request2 = buildRequest(request1.getTaskId(), request1.getType(), "Key",
        request1.getParameters().get("Key"));

    assertThat(request2.getTaskId()).isEqualTo(request1.getTaskId());
    assertThat(request2.getType()).isEqualTo(request1.getType());
    assertThat(request2.getParameters()).isEqualTo(request1.getParameters());
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
  public void equalsVerifier() throws Exception {
    MessageDtoRequestImpl request1 = buildRequest(1L, TaskType.LONG, "Key", "Value");

    MessageDtoRequestImpl request2 = new MessageDtoRequestImpl();
    request2.setTaskId(request1.getTaskId());
    assertThat(request1).isEqualTo(request2);

    request2.setTaskId(2L);
    assertThat(request1).isNotEqualTo(request2);

    request1.setTaskId(null);
    assertThat(request1).isNotEqualTo(request2);
  }
}
