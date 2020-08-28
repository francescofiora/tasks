package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.MessageDtoResponseImpl;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.jms.TaskResponder;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class SendMsgTasklet extends AbstractTasklet {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final TaskResponder taskResponder;
  
  public SendMsgTasklet(TaskResponder taskResponder) {
    super();
    this.taskResponder = taskResponder;
  }

  @Override
  void execute(String jobName, Long jobInstanceId, Map<String, Object> parameter,
      ExecutionContext executionContext) {
    log.info("SendMsgTasklet.execute() id:" + jobInstanceId);
    
    Task task = getTask(executionContext);
    
    taskResponder.send(new MessageDtoResponseImpl()
        .taskId(task.getTaskRef())
        .type(TaskType.valueOf(task.getTaskType()))
        .status(task.getStatus())
        .result(task.getResult()));
  }
}
