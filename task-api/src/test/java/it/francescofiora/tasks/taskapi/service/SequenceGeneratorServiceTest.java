package it.francescofiora.tasks.taskapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.taskapi.domain.DatabaseSequence;
import it.francescofiora.tasks.taskapi.service.impl.SequenceGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SequenceGeneratorServiceTest {

  private static final long ID = 100L;

  private SequenceGeneratorService sequenceGenerator;

  @MockBean
  private MongoOperations mongoOperations;

  /**
   * Set up.
   */
  @BeforeEach
  void setUp() {
    sequenceGenerator = new SequenceGeneratorServiceImpl(mongoOperations);
  }

  @Test
  void testGenerateSequence() throws Exception {
    when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class),
        any(FindAndModifyOptions.class), eq(DatabaseSequence.class))).thenReturn(null);

    long actual = sequenceGenerator.generateSequence("name");
    assertThat(actual).isEqualTo(1L);

    DatabaseSequence counter = new DatabaseSequence();
    counter.setSeq(ID);
    when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class),
        any(FindAndModifyOptions.class), eq(DatabaseSequence.class))).thenReturn(counter);

    actual = sequenceGenerator.generateSequence("name");
    assertThat(actual).isEqualTo(ID);
  }
}
