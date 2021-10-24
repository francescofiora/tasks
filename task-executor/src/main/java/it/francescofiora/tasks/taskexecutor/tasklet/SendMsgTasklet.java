package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.jms.JmsProducer;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Send Msg Tasklet.
 */
@Slf4j
@Component
@AllArgsConstructor
public class SendMsgTasklet extends AbstractTasklet {

  public static final String NAME = "sendMsgStep";

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
