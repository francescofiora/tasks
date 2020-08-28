package it.francescofiora.tasks.taskexecutor.jms.impl;

import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.taskexecutor.jms.TaskResponder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TaskResponderImpl implements TaskResponder {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @Override
  public void send(MessageDtoResponse response) {
    jmsTemplate.convertAndSend(destination, response);
    log.info("Producer Message " + response.getTaskId() + " Sent to " + destination);
  }

  @Value("${activemq.queue.response:QUEUE_RESPONSE}")
  private String destination;

  private final JmsTemplate jmsTemplate;

  public TaskResponderImpl(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }
}
