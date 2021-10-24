package it.francescofiora.tasks.taskapi.jms;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * Jms Consumer.
 */
@Slf4j
@Component
@AllArgsConstructor
public class JmsConsumer {

  private final JmsValidator validator;
  private final StrategyManager strategyManager;

  /**
   * Jms Listener.
   *
   * @param obj Object
   */
  @JmsListener(destination = "${activemq.queue.response:QUEUE_RESPONSE}")
  public void receiveMessage(Object obj) {
    log.debug("Message received: " + obj);

    var message = validator.validate(obj);
    log.debug("Message validated: " + message);

    strategyManager.exec(message);
    log.debug("Message executed: " + message);
  }

}
