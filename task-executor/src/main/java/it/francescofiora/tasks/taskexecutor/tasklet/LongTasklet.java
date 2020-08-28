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
public class LongTasklet extends AbstractTasklet {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
  
  private final TaskService taskService;

  public LongTasklet(TaskService taskService) {
    super();
    this.taskService = taskService;
  }

  @Override
  void execute(String jobName, Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("LongTasklet.execute() id:" + jobInstanceId);
    
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    Task task = getTask(executionContext);
    task.setStatus(TaskStatus.TERMINATED);
    task.setResult("Task Terminated");
    taskService.save(task);
  }

}
