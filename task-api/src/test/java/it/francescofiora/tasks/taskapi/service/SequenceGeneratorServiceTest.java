package it.francescofiora.tasks.taskapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import it.francescofiora.tasks.taskapi.domain.DatabaseSequence;
import it.francescofiora.tasks.taskapi.service.impl.SequenceGeneratorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

class SequenceGeneratorServiceTest {

  private static final long ID = 100L;

  @Test
  void testGenerateSequence() {
    var mongoOperations = mock(MongoOperations.class);

    when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class),
        any(FindAndModifyOptions.class), eq(DatabaseSequence.class))).thenReturn(null);

    var sequenceGenerator = new SequenceGeneratorServiceImpl(mongoOperations);
    var actual = sequenceGenerator.generateSequence("name");
    assertThat(actual).isEqualTo(1L);

    var counter = new DatabaseSequence();
    counter.setSeq(ID);
    when(mongoOperations.findAndModify(any(Query.class), any(UpdateDefinition.class),
        any(FindAndModifyOptions.class), eq(DatabaseSequence.class))).thenReturn(counter);

    actual = sequenceGenerator.generateSequence("name");
    assertThat(actual).isEqualTo(ID);
  }
}
