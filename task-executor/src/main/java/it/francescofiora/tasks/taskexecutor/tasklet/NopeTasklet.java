package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Nope Tasklet.
 */
@Slf4j
@Component
@AllArgsConstructor
public class NopeTasklet extends AbstractTasklet {

  public static final String NAME = "nopeStep";

  private final TaskService taskService;

  @Override
  void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("NopeTasklet.execute() id:" + jobInstanceId);

    var task = getTask(executionContext);
    task.setStatus(TaskStatus.ERROR);
    task.setResult("Type not supported!");
    taskService.save(task);
  }

}
