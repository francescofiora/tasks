package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Long Tasklet.
 */
@Slf4j
@Component
@AllArgsConstructor
public class LongTasklet extends AbstractTasklet {

  public static String NAME = "longTasklet";

  private final TaskService taskService;

  @Override
  void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("LongTasklet.execute() id:" + jobInstanceId);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      log.error(e.getMessage());
    }

    var task = getTask(executionContext);
    task.setStatus(TaskStatus.TERMINATED);
    task.setResult("Task Terminated");
    taskService.save(task);
  }
}
