package it.francescofiora.tasks.taskapi.jms;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
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
public class JmsConsumerTest {

  private static final MessageDtoResponse MSG_SENT = new MessageDtoResponseImpl().taskId(1L);

  @Value("${activemq.queue.response:QUEUE_RESPONSE}")
  private String destination;

  private static final JmsMessage MSS_VALIDATED =
      new JmsMessage(MSG_SENT, "ID", new Date().getTime());

  @Autowired
  private JmsTemplate template;

  @Autowired
  private StrategyManager strategyManager;

  @Test
  public void testReceiveMessage() throws Exception {
    template.convertAndSend(destination, MSG_SENT);
    verify(strategyManager, timeout(1000)).exec(eq(MSS_VALIDATED));
  }

  @TestConfiguration
  @EnableJms
  static class TestContextConfiguration {

    @Bean
    public MessageConverter messageConverter() {
      MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
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
      JmsTemplate template = new JmsTemplate(connectionFactory);
      template.setMessageConverter(messageConverter);
      return template;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(
        ConnectionFactory connectionFactory, MessageConverter messageConverter) {
      DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
      factory.setMessageConverter(messageConverter);
      factory.setConnectionFactory(connectionFactory);

      return factory;
    }

    @Bean
    public StrategyManager strategyManager() {
      return spy(mock(StrategyManager.class));
    }

    @Bean
    public JmsValidator validator() {
      JmsValidator validator = mock(JmsValidator.class);
      when(validator.validate(any(ActiveMQTextMessage.class))).thenReturn(MSS_VALIDATED);
      return validator;
    }

    @Bean
    public JmsConsumer jmsConsumer(JmsValidator validator, StrategyManager strategyManager) {
      return new JmsConsumer(validator, strategyManager);
    }
  }
}