package it.francescofiora.tasks.taskapi.jms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.taskapi.jms.errors.JmsException;
import it.francescofiora.tasks.taskapi.jms.impl.JmsValidatorImpl;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import java.util.Date;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;

class JmsValidatorTest {

  private static final String ID = "ID-ActiveMq-Message";

  private ObjectMapper mapper = new ObjectMapper();

  @Test
  void testValidate() throws Exception {
    var response = TestUtils.createMessageDtoResponse();
    var amqMessage = new ActiveMQTextMessage();
    amqMessage.setJMSMessageID(ID);
    amqMessage.setTimestamp(new Date().getTime());
    amqMessage.setText(mapper.writeValueAsString(response));

    var validator = new JmsValidatorImpl(mapper);
    var message = validator.validate(amqMessage);

    assertThat(message.getJmsMessageId()).isEqualTo(amqMessage.getJMSMessageID());
    assertThat(message.getTimestamp()).isEqualTo(amqMessage.getTimestamp());
    assertThat(message.getResponse()).isEqualTo(response);
  }

  @Test
  void testValidateBadMessage() {
    var validator = new JmsValidatorImpl(mapper);
    assertThrows(JmsException.class, () -> validator.validate(null));
    assertThrows(JmsException.class, () -> validator.validate(new Object()));
    assertThrows(JmsException.class, () -> validator.validate(new ActiveMQTextMessage()));
  }
}
