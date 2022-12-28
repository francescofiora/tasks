package it.francescofiora.tasks.taskapi.service;

import it.francescofiora.tasks.message.MessageDtoResponse;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Task Service.
 */
public interface TaskService {

  /**
   * Create a task.
   *
   * @param taskDto the entity to create.
   * @return the persisted entity.
   */
  TaskDto create(NewTaskDto taskDto);

  /**
   * Patch a task.
   *
   * @param taskDto the entity to patch.
   */
  void patch(UpdatableTaskDto taskDto);

  /**
   * Get all the tasks.
   *
   * @param description the description
   * @param type the type of the task
   * @param status the status of the task
   * @param pageable the pagination information.
   * @return the list of entities.
   */
  Page<TaskDto> findAll(String description, TaskType type, TaskStatus status, Pageable pageable);

  /**
   * Get the "id" task.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<TaskDto> findOne(Long id);

  /**
   * Delete the "id" task.
   *
   * @param id the id of the entity.
   */
  void delete(Long id);

  /**
   * handle response.
   *
   * @param response MessageDtoResponse
   */
  void response(MessageDtoResponse response);
}
