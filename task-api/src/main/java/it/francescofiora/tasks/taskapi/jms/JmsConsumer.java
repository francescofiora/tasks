package it.francescofiora.tasks.taskapi.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final JmsValidator validator;

  private final StrategyManager strategyManager;

  /**
   * Constructor.
   * 
   * @param validator       JmsValidator
   * @param strategyManager StrategyManager
   */
  public JmsConsumer(JmsValidator validator, StrategyManager strategyManager) {
    this.validator = validator;
    this.strategyManager = strategyManager;
  }

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
