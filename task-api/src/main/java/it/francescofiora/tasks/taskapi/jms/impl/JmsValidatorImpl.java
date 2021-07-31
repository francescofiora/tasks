package it.francescofiora.tasks.taskapi.jms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.taskapi.jms.JmsValidator;
import it.francescofiora.tasks.taskapi.jms.errors.JmsException;
import it.francescofiora.tasks.taskapi.jms.message.JmsMessage;
import lombok.AllArgsConstructor;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Jms Validator Impl.
 */
@Component
@AllArgsConstructor
public class JmsValidatorImpl implements JmsValidator {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  private final ObjectMapper mapper;

  @Override
  public JmsMessage validate(Object obj) {
    if (obj instanceof ActiveMQTextMessage) {
      var txtMessage = (ActiveMQTextMessage) obj;

      MessageDtoResponse response = null;
      try {
        response = mapper.readValue(txtMessage.getText(), MessageDtoResponseImpl.class);
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new JmsException(e.getMessage(), e);
      }

      return new JmsMessage(response, txtMessage.getJMSMessageID(), txtMessage.getTimestamp());
    }

    throw new JmsException(obj + " not valid");
  }

}
