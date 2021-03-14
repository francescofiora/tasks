package it.francescofiora.tasks.taskexecutor.repository;

import it.francescofiora.tasks.taskexecutor.domain.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long> {

}
