package it.francescofiora.tasks.taskexecutor.tasklet;

import it.francescofiora.tasks.taskexecutor.domain.Task;
import java.util.Map;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public abstract class AbstractTasklet implements Tasklet {

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
      throws Exception {

    final var executionContext =
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    final var stepContext = chunkContext.getStepContext();
    final var jobInstanceId = stepContext.getJobInstanceId();
    final var jobParameters = stepContext.getJobParameters();

    execute(jobInstanceId, jobParameters, executionContext);

    return RepeatStatus.FINISHED;
  }

  abstract void execute(Long jobInstanceId, Map<String, Object> jobParameters,
      ExecutionContext executionContext);

  protected String getJmsMessageId(Map<String, Object> jobParameters) {
    return getString(jobParameters, JmsParameters.JMS_MESSAGE_ID);
  }


  protected String getJobType(Map<String, Object> jobParameters) {
    return getString(jobParameters, JmsParameters.JOB_TYPE);
  }

  protected Long getTaskRef(Map<String, Object> jobParameters) {
    return getLong(jobParameters, JmsParameters.TASK_REF);
  }

  protected String getTaskType(Map<String, Object> jobParameters) {
    return getString(jobParameters, JmsParameters.TASK_TYPE);
  }

  protected Long getMessageCreated(Map<String, Object> jobParameters) {
    return getLong(jobParameters, JmsParameters.MESSAGE_CREATED);
  }

  protected String getString(Map<String, Object> jobParameters, String key) {
    if (jobParameters.get(key) instanceof String) {
      return (String) jobParameters.get(key);
    }
    return null;
  }

  protected String toString(Map<String, Object> jobParameters, String key) {
    if (jobParameters.get(key) == null) {
      return null;
    }
    return String.valueOf(jobParameters.get(key));
  }

  protected Long getLong(Map<String, Object> jobParameters, String key) {
    if (jobParameters.get(key) instanceof Long) {
      return (Long) jobParameters.get(key);
    }
    return null;
  }

  public static final String TASK = "TASK";

  protected void setTask(ExecutionContext executionContext, Task task) {
    executionContext.put(TASK, task);
  }

  protected Task getTask(ExecutionContext executionContext) {
    return (Task) executionContext.get(TASK);
  }
}
