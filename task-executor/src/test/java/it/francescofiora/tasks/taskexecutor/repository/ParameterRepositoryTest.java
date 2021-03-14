package it.francescofiora.tasks.taskexecutor.repository;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ParameterRepositoryTest extends AbstractTestRepository {

  @Autowired
  private ParameterRepository parameterRepository;

  @Test
  public void testCrud() throws Exception {
    Parameter expected = new Parameter().name("name").value("value");
    expected = parameterRepository.save(expected);
    
    Optional<Parameter> opt = parameterRepository.findById(expected.getId());
    assertThat(opt).isPresent().get().isEqualTo(expected);
    
    parameterRepository.delete(expected);
    opt = parameterRepository.findById(expected.getId());
    assertThat(opt).isNotPresent();
  }

}
