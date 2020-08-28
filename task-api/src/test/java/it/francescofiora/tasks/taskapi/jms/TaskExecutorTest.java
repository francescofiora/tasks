package it.francescofiora.tasks.taskapi.jms;

import static org.assertj.core.api.Assertions.assertThat;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.taskapi.jms.impl.TaskExecutorImpl;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = { "classpath:application_test.properties" })
public class TaskExecutorTest {

  @Autowired
  private TaskExecutor taskExecutor;

  @Test
  public void testSend() {
    MessageDtoRequest request = new MessageDtoRequestImpl();
    taskExecutor.send(request);
  }

  @TestConfiguration
  @EnableJms
  static class TestContextConfiguration {

    @Bean
    public JmsTemplate jmsTemplate() {
      ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
      return new JmsTemplate(connectionFactory);
    }
    
    @Bean
    public TaskExecutor taskExecutor(JmsTemplate jmsTemplate) {
      return new TaskExecutorImpl(jmsTemplate);
    }

  }

}
