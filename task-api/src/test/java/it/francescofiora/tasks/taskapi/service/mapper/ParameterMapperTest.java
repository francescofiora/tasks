package it.francescofiora.tasks.taskapi.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParameterMapperTest {

  private ParameterMapper parameterMapper;

  @BeforeEach
  void setUp() {
    parameterMapper = new ParameterMapperImpl();
  }

  @Test
  void testNullObject() {
    assertThat(parameterMapper.toEntity(null)).isNull();
  }
  
}
