package it.francescofiora.tasks.taskapi.jms.impl;

import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.taskapi.jms.JmsProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 * Jms Producer Impl.
 */
@Component
public class JmsProducerImpl implements JmsProducer {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @Value("${activemq.queue.request:QUEUE_REQUEST}")
  private String destination;

  private final JmsTemplate jmsTemplate;

  public JmsProducerImpl(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }

  @Override
  public void send(MessageDtoRequest request) {
    jmsTemplate.convertAndSend(destination, request);
    log.info("Producer Message " + request.getTaskId() + " Sent to " + destination);
  }

}
