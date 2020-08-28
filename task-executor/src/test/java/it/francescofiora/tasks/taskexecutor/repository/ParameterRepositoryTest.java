package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;

public class ParameterRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ParameterRepository parameterRepository;

  @Test
  public void testCRUD() throws Exception {
    Parameter expected = new Parameter().name("name").value("value");
    expected = parameterRepository.save(expected);
    
    Optional<Parameter> opt = parameterRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);
    
    parameterRepository.delete(expected);
    opt = parameterRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }

}
