package it.francescofiora.tasks.taskexecutor.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.Test;

/**
 * OpenApi Config Test.
 */
class OpenApiConfigTest {

  @Test
  void testOpenApiConfig() {
    var openApi = new OpenApiConfig().customOpenApi();

    assertThat(openApi.getInfo().getTitle()).isEqualTo("Tasks-Executor Demo App");
    assertThat(openApi.getInfo().getDescription())
        .isEqualTo("This is a sample Spring Boot RESTful service");
  }

  @Test
  void testCustomGlobalHeaders() {
    var operationCustomizer = new OpenApiConfig().customGlobalHeaders();

    var operation = operationCustomizer.customize(mock(Operation.class), null);
    verify(operation).addParametersItem(any(Parameter.class));
  }
}
