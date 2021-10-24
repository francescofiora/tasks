package it.francescofiora.tasks.taskexecutor.jms.impl;

import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.taskexecutor.jms.JmsProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Jms Producer Impl.
 */
@Slf4j
@Component
public class JmsProducerImpl implements JmsProducer {

  @Value("${activemq.queue.response:QUEUE_RESPONSE}")
  private String destination;

  private final JmsTemplate jmsTemplate;

  public JmsProducerImpl(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  @Override
  public void send(MessageDtoResponse response) {
    jmsTemplate.convertAndSend(destination, response);
    log.info("Producer Message " + response.getTaskId() + " Sent to " + destination);
  }
}
