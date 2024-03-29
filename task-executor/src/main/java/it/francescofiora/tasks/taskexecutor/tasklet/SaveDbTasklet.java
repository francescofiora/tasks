package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import it.francescofiora.tasks.taskexecutor.domain.Task;
import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.tasklet.errors.TaskletException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

/**
 * Save to Db Tasklet.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SaveDbTasklet extends AbstractTasklet {

  public static final String NAME = "saveDbStep";

  private final TaskService taskService;

  @Override
  void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext) {
    log.info("SaveDbTasklet.execute() id:" + jobInstanceId);

    var taskOpt = taskService.findByTaskRef(getTaskRef(jobParameters));
    Task task = null;
    if (taskOpt.isPresent()) {
      task = taskOpt.get();
      if (TaskStatus.IN_PROGRESS.equals(task.getStatus())) {
        throw new TaskletException("Task already in progress");
      }
    } else {
      task = new Task().taskRef(getTaskRef(jobParameters));
      task.setParameters(jobParameters.keySet().stream()
          .map(key -> new Parameter().name(key).value(toString(jobParameters, key)))
          .filter(parameter -> parameter.getValue() != null).collect(Collectors.toSet()));
    }
    task.setJmsMessageId(getJmsMessageId(jobParameters));
    task.setJobName(JobType.valueOf(getJobType(jobParameters)));
    task.setTaskType(getTaskType(jobParameters));
    task.setMessageCreated(new Timestamp(getMessageCreated(jobParameters)));
    task.setJobInstanceId(jobInstanceId);
    task.setStatus(TaskStatus.IN_PROGRESS);

    task = taskService.save(task);
    setTask(executionContext, task);
  }
}
