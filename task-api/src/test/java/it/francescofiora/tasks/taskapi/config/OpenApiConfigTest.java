package it.francescofiora.tasks.taskapi.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * OpenApi Config Test.
 */
class OpenApiConfigTest {

  @Test
  void testOpenApiConfig() {
    var openApi = new OpenApiConfig().customOpenApi();

    assertThat(openApi.getInfo().getTitle()).isEqualTo("Tasks-Api Demo App");
    assertThat(openApi.getInfo().getDescription())
        .isEqualTo("This is a sample Spring Boot RESTful service");
  }
}
