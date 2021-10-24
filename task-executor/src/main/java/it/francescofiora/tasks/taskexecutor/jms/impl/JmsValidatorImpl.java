package it.francescofiora.tasks.taskexecutor.jms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.francescofiora.tasks.message.MessageDtoRequest;
import it.francescofiora.tasks.message.MessageDtoRequestImpl;
import it.francescofiora.tasks.taskexecutor.jms.JmsValidator;
import it.francescofiora.tasks.taskexecutor.jms.errors.JmsException;
import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.stereotype.Component;

/**
 * Jms Validator Impl.
 */
@Slf4j
@Component
@AllArgsConstructor
public class JmsValidatorImpl implements JmsValidator {
  
  private final ObjectMapper mapper;

  @Override
  public JmsMessage validate(Object obj) {
    if (obj instanceof ActiveMQTextMessage) {
      var txtMessage = (ActiveMQTextMessage) obj;

      MessageDtoRequest request = null;
      try {
        request = mapper.readValue(txtMessage.getText(), MessageDtoRequestImpl.class);
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new JmsException(e.getMessage(), e);
      }

      return new JmsMessage(request, txtMessage.getJMSMessageID(), txtMessage.getTimestamp());
    }

    throw new JmsException(obj + " not valid");
  }

}
