package it.francescofiora.tasks.taskexecutor.tasklet;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;

/**
 * Abstract Tasklet For Tests.
 */
public class AbstractTaskletTest {

  private static final String KEY = "test";
  
  static class DummyTasklet extends AbstractTasklet {

    @Override
    void execute(Long jobInstanceId, Map<String, Object> jobParameters,
        ExecutionContext executionContext) {}
  }

  @Test
  void testTasklet() {
    var tasklet = new DummyTasklet();
    assertThat(tasklet.toString(Collections.emptyMap(), KEY)).isNull();

    assertThat(tasklet.getLong(Collections.emptyMap(), KEY)).isNull();
  }
}
