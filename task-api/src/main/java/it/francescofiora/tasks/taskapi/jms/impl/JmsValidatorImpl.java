package it.francescofiora.tasks.taskapi.jms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.jms.JmsEvent;
import it.francescofiora.tasks.taskapi.jms.JmsValidator;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class JmsValidatorImpl implements JmsValidator {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  private final ObjectMapper mapper;

  public JmsValidatorImpl(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public JmsEvent validate(Object obj) {
    if (obj instanceof ActiveMQTextMessage) {
      ActiveMQTextMessage txtMessage = (ActiveMQTextMessage) obj;

      MessageDtoResponse response = null;
      try {
        response = mapper.readValue(txtMessage.getText(), MessageDtoResponseImpl.class);
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new RuntimeException(e.getMessage());
      }

      return new JmsEvent(response, txtMessage.getJMSMessageID(), txtMessage.getTimestamp());
    }

    throw new RuntimeException(obj + " not valid");
  }

}
