package it.francescofiora.tasks.taskexecutor.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;
import java.util.Date;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = {"classpath:application_test.properties"})
class JmsConsumerTest {

  private static final MessageDtoRequest MSG_SENT = new MessageDtoRequestImpl().taskId(1L);

  @Value("${activemq.queue.request:QUEUE_REQUEST}")
  private String destination;

  private static final JmsMessage MSG_VALIDATED =
      new JmsMessage(MSG_SENT, "ID", new Date().getTime());

  @Autowired
  private JmsTemplate template;

  @Autowired
  private StrategyManager strategyManager;

  @Test
  void testReceiveMessage() throws Exception {
    template.convertAndSend(destination, MSG_SENT);
    verify(strategyManager, timeout(1000)).exec(MSG_VALIDATED);
  }

  @TestConfiguration
  @EnableJms
  static class TestContextConfiguration {

    @Bean
    public MessageConverter messageConverter() {
      var converter = new MappingJackson2MessageConverter();
      converter.setTargetType(MessageType.TEXT);
      converter.setTypeIdPropertyName(MessageDto.class.getName());
      return converter;
    }

    @Bean
    public ConnectionFactory connectionFactory(
        @Value("${spring.activemq.broker-url}") String brokerUrl) {
      return new ActiveMQConnectionFactory(brokerUrl);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory,
        MessageConverter messageConverter) {
      var template = new JmsTemplate(connectionFactory);
      template.setMessageConverter(messageConverter);
      return template;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
        ConnectionFactory connectionFactory, MessageConverter messageConverter) {
      var factory = new DefaultJmsListenerContainerFactory();
      factory.setMessageConverter(messageConverter);
      factory.setConnectionFactory(connectionFactory);

      return factory;
    }

    @Bean
    public StrategyManager strategyManager() {
      return mock(StrategyManager.class);
    }

    @Bean
    public JmsValidator validator() {
      var validator = mock(JmsValidator.class);
      when(validator.validate(any(ActiveMQTextMessage.class))).thenReturn(MSG_VALIDATED);
      return validator;
    }

    @Bean
    public JmsConsumer jmsConsumer(JmsValidator validator, StrategyManager strategyManager) {
      return new JmsConsumer(validator, strategyManager);
    }
  }
}
