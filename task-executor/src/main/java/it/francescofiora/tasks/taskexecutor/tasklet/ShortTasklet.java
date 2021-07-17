package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ShortTasklet extends AbstractTasklet {

  public static final String NAME = "shortStep";

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final TaskService taskService;

  @Override
  void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("ShortTasklet.execute() id:" + jobInstanceId);

    try {
      Thread.sleep(100);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    var task = getTask(executionContext);
    task.setStatus(TaskStatus.TERMINATED);
    task.setResult("Task Terminated");
    taskService.save(task);
  }

}
