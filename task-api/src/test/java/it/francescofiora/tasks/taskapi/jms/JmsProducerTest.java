package it.francescofiora.tasks.taskapi.jms;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.taskapi.jms.impl.JmsProducerImpl;
import it.francescofiora.tasks.taskapi.util.TaskTestListener;
import it.francescofiora.tasks.taskapi.util.TestUtils;
import java.util.concurrent.TimeUnit;
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
class JmsProducerTest {

  @Autowired
  private JmsProducer jmsProducer;

  @Autowired
  private TaskTestListener listener;

  @Test
  void testSend() throws Exception {
    Long count = listener.getLatch().getCount();

    MessageDtoRequest request = TestUtils.createMessageDtoRequest();
    jmsProducer.send(request);

    listener.getLatch().await(10000, TimeUnit.MILLISECONDS);

    assertThat(listener.getLatch().getCount()).isEqualTo(count - 1);

    ActiveMQTextMessage message = (ActiveMQTextMessage) listener.getObj();
    MessageDtoRequest actual =
        new ObjectMapper().readValue(message.getText(), MessageDtoRequestImpl.class);
    assertThat(actual).isEqualTo(request);
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
    public JmsProducer jmsProducer(JmsTemplate jmsTemplate) {
      return new JmsProducerImpl(jmsTemplate);
    }

    @Bean
    public TaskTestListener taskTestListener() {
      return new TaskTestListener();
    }

  }

}
