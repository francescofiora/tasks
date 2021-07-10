package it.francescofiora.tasks.taskexecutor.config;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;

public class BatchConfigurer extends DefaultBatchConfigurer {

  private final DataSource dataSource;
  
  private static final String TABLE_PREFIX = "BATCH_";

  public BatchConfigurer(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
  }

  @Override
  protected JobRepository createJobRepository() throws Exception {
    var factory = new JobRepositoryFactoryBean();
    factory.setDataSource(dataSource);
    factory.setTransactionManager(getTransactionManager());
    factory.setIsolationLevelForCreate("ISOLATION_REPEATABLE_READ");
    factory.setTablePrefix(TABLE_PREFIX);
    return factory.getObject();
  }

  @Override
  protected JobExplorer createJobExplorer() throws Exception {
    var jobExplorerFactoryBean = new JobExplorerFactoryBean();
    jobExplorerFactoryBean.setDataSource(this.dataSource);
    jobExplorerFactoryBean.afterPropertiesSet();
    jobExplorerFactoryBean.setTablePrefix(TABLE_PREFIX);
    return jobExplorerFactoryBean.getObject();
  }
}
