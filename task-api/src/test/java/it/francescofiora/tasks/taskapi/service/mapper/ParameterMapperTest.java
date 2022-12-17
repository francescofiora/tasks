package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ParameterMapperTest {

  @Test
  void testNullObject() {
    var parameterMapper = new ParameterMapperImpl();
    assertThat(parameterMapper.toEntity(null)).isNull();
  }
  
}
