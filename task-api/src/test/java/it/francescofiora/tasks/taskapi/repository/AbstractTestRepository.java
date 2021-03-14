package it.francescofiora.tasks.taskapi.repository;

import it.francescofiora.tasks.taskapi.service.SequenceGeneratorService;
import it.francescofiora.tasks.taskapi.service.impl.SequenceGeneratorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public abstract class AbstractTestRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  @Autowired
  private MongoOperations mongoOperations;
  
  private SequenceGeneratorService sequenceGenerator;
  
  @BeforeEach
  public void setUp() {
    sequenceGenerator = new SequenceGeneratorServiceImpl(mongoOperations);
  }
  
  protected Long generateSequence(String sequenceName) {
    return sequenceGenerator.generateSequence(sequenceName);
  }

  
  protected MongoTemplate getMongoTemplate() {
    return mongoTemplate;
  }
}
