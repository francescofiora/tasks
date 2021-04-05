package it.francescofiora.tasks.taskapi.jms.message;

import static org.assertj.core.api.Assertions.assertThat;

import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import org.junit.jupiter.api.Test;

public class JmsMessageTest {

  private static final String ID = "123456789";
  private static final Long TIMESTAMP = 1000L;
  private static final MessageDtoResponse RESPONSE = TestUtils.createMessageDtoResponse();

  @Test
  public void testDtoStructureAndBehavior() {
    // @formatter:off
    Validator validator = ValidatorBuilder
        .create()
        .with(new GetterMustExistRule())
        .with(new GetterTester())
        .build();
    // @formatter:on

    validator.validate(PojoClassFactory.getPojoClass(JmsMessage.class));
  }

  @Test
  public void testBuilder() {
    JmsMessage message = new JmsMessage(RESPONSE, ID, TIMESTAMP);

    assertThat(message.getJmsMessageId()).isEqualTo(ID);
    assertThat(message.getTimestamp()).isEqualTo(TIMESTAMP);
    assertThat(message.getResponse()).isEqualTo(RESPONSE);
  }

  @Test
  public void equalsVerifier() throws Exception {
    JmsMessage message1 = new JmsMessage(RESPONSE, ID, TIMESTAMP);
    TestUtils.checkEqualHashAndToString(message1, message1);
    assertThat(message1.equals(null)).isFalse();
    assertThat(message1.equals(new Object())).isFalse();

    JmsMessage message2 = new JmsMessage(RESPONSE, ID, TIMESTAMP);
    TestUtils.checkEqualHashAndToString(message1, message2);

    message2 = new JmsMessage(RESPONSE, "notequals", TIMESTAMP);
    TestUtils.checkNotEqualHashAndToString(message1, message2);

    message2 = new JmsMessage(RESPONSE, null, TIMESTAMP);
    TestUtils.checkNotEqualHashAndToString(message1, message2);
  }
}