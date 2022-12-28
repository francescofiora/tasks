package it.francescofiora.tasks.taskexecutor.jms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.taskexecutor.jms.errors.JmsException;
import it.francescofiora.tasks.taskexecutor.jms.impl.JmsValidatorImpl;
import it.francescofiora.tasks.taskexecutor.util.TestUtils;
import java.util.Date;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;

class JmsValidatorTest {

  private static final String ID = "ID-ActiveMq-Message";

  @Test
  void testValidate() throws Exception {
    var request = TestUtils.createMessageDtoRequest();
    var amqMessage = new ActiveMQTextMessage();
    amqMessage.setJMSMessageID(ID);
    amqMessage.setTimestamp(new Date().getTime());
    var mapper = new ObjectMapper();
    amqMessage.setText(mapper.writeValueAsString(request));

    var validator = new JmsValidatorImpl(mapper);
    var message = validator.validate(amqMessage);

    assertThat(message.getJmsMessageId()).isEqualTo(amqMessage.getJMSMessageID());
    assertThat(message.getTimestamp()).isEqualTo(amqMessage.getTimestamp());
    assertThat(message.getRequest()).isEqualTo(request);
  }

  @Test
  void testValidateBadMessage() {
    var validator = new JmsValidatorImpl(new ObjectMapper());
    assertThrows(JmsException.class, () -> validator.validate(null));
    assertThrows(JmsException.class, () -> validator.validate(new Object()));
    assertThrows(JmsException.class, () -> validator.validate(new ActiveMQTextMessage()));
  }
}
