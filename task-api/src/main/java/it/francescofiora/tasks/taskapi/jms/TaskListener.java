package it.francescofiora.tasks.taskapi.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TaskListener {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final JmsValidator validator;

  private final StrategyManager strategyManager;

  /**
   * constructor.
   * 
   * @param validator       JmsValidator
   * @param strategyManager StrategyManager
   */
  public TaskListener(JmsValidator validator, StrategyManager strategyManager) {
    super();
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
    log.info("Receiver Message " + obj);

    JmsEvent event = validator.validate(obj);
    log.debug("Validated Message " + event);

    strategyManager.exec(event);
    log.debug("Executed Message " + event);
  }

}
