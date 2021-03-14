package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class NopeTasklet extends AbstractTasklet {

  public static final String NAME = "nopeStep";

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final TaskService taskService;

  public NopeTasklet(TaskService taskService) {
    super();
    this.taskService = taskService;
  }

  @Override
  void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("NopeTasklet.execute() id:" + jobInstanceId);

    Task task = getTask(executionContext);
    task.setStatus(TaskStatus.ERROR);
    task.setResult("Type not supported!");
    taskService.save(task);
  }

}
