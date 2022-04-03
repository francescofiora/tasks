package it.francescofiora.tasks.taskexecutor.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task Executor Api.
 */
@RestController
@Tag(name = "task-executor", description = "Task Executor Rest API")
@RequestMapping("/api")
public class TaskExecutorApi extends AbstractApi {

  private static final String ENTITY_NAME = "TaskExecutorDto";

  private final TaskService taskService;

  public TaskExecutorApi(TaskService taskService) {
    super(ENTITY_NAME);
    this.taskService = taskService;
  }

  /**
   * {@code GET  /tasks} : get all the tasks.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
   */
  @Operation(summary = "Searches tasks",
      description = "By passing in the appropriate options, "
          + "you can search for available tasks in the system",
      tags = {"task"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = TaskExecutorDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/tasks")
  public ResponseEntity<List<TaskExecutorDto>> getAllTasks(Pageable pageable) {
    return getResponse(taskService.findAll(pageable));
  }

  /**
   * {@code GET  /tasks/:id} : get the "id" task.
   *
   * @param id the id of the taskDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskDto, or
   *         with status {@code 404 (Not Found)}.
   */
  @Operation(summary = "Searches task by 'id'", description = "Searches task by 'id'",
      tags = {"task"})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = TaskExecutorDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @GetMapping("/tasks/{id}")
  public ResponseEntity<TaskExecutorDto> getTask(@Parameter(description = "id of the task to get",
      required = true, example = "1") @PathVariable Long id) {
    return getResponse(taskService.findOne(id), id);
  }

  /**
   * {@code DELETE  /tasks} : Deletes an existing task.
   *
   * @param id the id of the taskDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} or with status
   *         {@code 400 (Bad Request)} if the taskDto is not valid, or with status
   *         {@code 500 (Internal Server Error)} if the taskDto couldn't be deleted.
   */
  @Operation(summary = "Delete Task", description = "Delete an Task to the system", tags = {"task"})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Task deleted"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<Void> deleteTask(@Parameter(description = "Id of the task to delete",
      required = true, example = "1") @PathVariable Long id) {
    taskService.delete(id);
    return deleteResponse(id);
  }
}
