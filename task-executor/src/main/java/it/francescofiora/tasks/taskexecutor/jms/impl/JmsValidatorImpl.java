package it.francescofiora.tasks.taskexecutor.jms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.taskexecutor.jms.JmsEvent;
import it.francescofiora.tasks.taskexecutor.jms.JmsValidator;
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

      MessageDtoRequest request = null;
      try {
        request = mapper.readValue(txtMessage.getText(), MessageDtoRequestImpl.class);
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new RuntimeException(e.getMessage());
      }

      return new JmsEvent(request, txtMessage.getJMSMessageID(), txtMessage.getTimestamp());
    }

    throw new RuntimeException(obj + " not valid");
  }

}
