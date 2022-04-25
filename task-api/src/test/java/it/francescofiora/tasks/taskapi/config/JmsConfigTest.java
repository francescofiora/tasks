package it.francescofiora.tasks.taskapi.config;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.config.parameter.JmsProperties;
import it.francescofiora.tasks.taskapi.config.parameter.SslProperties;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;

/**
 * JmsConfig Test.
 */
class JmsConfigTest {

  @Test
  void testJacksonJmsMessageConverter() throws Exception {
    var converter = new JmsConfig().jacksonJmsMessageConverter();
    var message = new ActiveMQTextMessage();

    var msg =
        new MessageDtoRequestImpl().type(TaskType.LONG).taskId(1L).addParameter("key", "value");

    message.setProperty(MessageDto.class.getName(), MessageDtoRequestImpl.class.getName());
    message.setText(new ObjectMapper().writeValueAsString(msg));
    var result = converter.fromMessage(message);
    assertThat(result).isNotNull();
  }

  @Test
  void testConnectionFactory() throws Exception {
    var properties = new JmsProperties();
    properties.setBrokerUrl("tcp://localhost:61616");
    properties.setUser("user");
    var ssl = new SslProperties();
    ssl.setKeyStorePass("KeyStorePass");
    ssl.setKeyStorePath("KeyStorePath");
    ssl.setTrustStorePass("TrustStorePass");
    ssl.setTrustStorePath("TrustStorePath");
    properties.setSsl(ssl);
    var config = new JmsConfig();
    var connectionFactory = (ActiveMQSslConnectionFactory) config.connectionFactory(properties);
    assertThat(connectionFactory.getBrokerURL()).isEqualTo(properties.getBrokerUrl());
    assertThat(connectionFactory.getUserName()).isEqualTo(properties.getUser());
    assertThat(connectionFactory.getTrustStore())
        .isEqualTo(properties.getSsl().getTrustStorePath());
    assertThat(connectionFactory.getTrustStorePassword())
        .isEqualTo(properties.getSsl().getTrustStorePass());
    assertThat(connectionFactory.getKeyStore()).isEqualTo(properties.getSsl().getKeyStorePath());
    assertThat(connectionFactory.getKeyStorePassword())
        .isEqualTo(properties.getSsl().getKeyStorePass());
  }
}
