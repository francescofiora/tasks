package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.jms.JmsProducer;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SendMsgTasklet extends AbstractTasklet {

  public static final String NAME = "sendMsgStep";

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final JmsProducer jmsProducer;

  @Override
  void execute(Long jobInstanceId, Map<String, Object> parameter,
      ExecutionContext executionContext) {
    log.info("SendMsgTasklet.execute() id:" + jobInstanceId);

    var task = getTask(executionContext);

    jmsProducer.send(new MessageDtoResponseImpl().taskId(task.getTaskRef())
        .type(TaskType.valueOf(task.getTaskType())).status(task.getStatus())
        .result(task.getResult()));
  }
}
