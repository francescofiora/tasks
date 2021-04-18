package it.francescofiora.tasks.taskexecutor.jms.impl;

import it.francescofiora.tasks.taskexecutor.domain.enumeration.JobType;
import it.francescofiora.tasks.taskexecutor.jms.StrategyManager;
import it.francescofiora.tasks.taskexecutor.jms.errors.JmsException;
import it.francescofiora.tasks.taskexecutor.jms.message.JmsMessage;
import it.francescofiora.tasks.taskexecutor.tasklet.JmsParameters;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
public class StrategyManagerImpl implements StrategyManager {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private final Map<String, Job> map = new HashMap<>();
  private final JobLauncher jobLauncher;

  /**
   * constructor.
   * 
   * @param jobLauncher JobLauncher
   * @param jobs        Job[]
   */
  public StrategyManagerImpl(JobLauncher jobLauncher, Job[] jobs) {
    this.jobLauncher = jobLauncher;
    for (Job job : jobs) {
      map.put(job.getName(), job);
    }
  }

  @Override
  public void exec(JmsMessage event) {
    final String type = event.getRequest().getType().name();
    Job job = map.containsKey(type) ? map.get(type) : map.get(JobType.NOPE.name());

    log.info("Executor - " + job.getName());
    try {
      JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
          .addString(JmsParameters.JMS_MESSAGE_ID, event.getJmsMessageId())
          .addString(JmsParameters.TASK_TYPE, type)
          .addLong(JmsParameters.TASK_REF, event.getRequest().getTaskId())
          .addLong(JmsParameters.MESSAGE_CREATED, event.getTimestamp())
          .addString(JmsParameters.JOB_TYPE, job.getName());

      event.getRequest().getParameters()
          .forEach((key, value) -> jobParametersBuilder.addString(key, value));

      jobLauncher.run(job, jobParametersBuilder.toJobParameters());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new JmsException(e.getMessage(), e);
    }
  }
}
