package it.francescofiora.tasks.taskapi.repository;

import it.francescofiora.tasks.taskapi.domain.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Task entity.
 */
@Repository
public interface TaskRepository extends MongoRepository<Task, Long> {
}
