package it.francescofiora.tasks.taskexecutor.repository;

import it.francescofiora.tasks.taskexecutor.domain.Task;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  Optional<Task> findByTaskRef(Long taskRef);
}
