package it.francescofiora.tasks.taskexecutor.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import it.francescofiora.tasks.taskexecutor.service.TaskService;
import it.francescofiora.tasks.taskexecutor.service.dto.TaskExecutorDto;
import it.francescofiora.tasks.taskexecutor.web.util.HeaderUtil;
import it.francescofiora.tasks.taskexecutor.web.util.PaginationUtil;
import it.francescofiora.tasks.taskexecutor.web.util.ResponseUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "task-executor", description = "Task Executor Rest API")
@RequestMapping("/api")
public class TaskExecutorApi {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  private static final String ENTITY_NAME = "Task-Executor";

  private final TaskService taskService;

  public TaskExecutorApi(TaskService taskService) {
    super();
    this.taskService = taskService;
  }

  /**
   * {@code GET  /tasks} : get all the tasks.
   *
   * @param pageable the pagination information.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
   *         of tasks in body.
   */
  @Operation(
      summary = "searches tasks", description = "By passing in the appropriate options, "
          + "you can search for available tasks in the system",
      tags = { "task" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(
                  array = @ArraySchema(schema = @Schema(implementation = TaskExecutorDto.class)))),
          @ApiResponse(responseCode = "400", description = "bad input parameter") })
  @GetMapping("/tasks")
  public ResponseEntity<List<TaskExecutorDto>> getAllTasks(Pageable pageable) {
    log.debug("REST request to get a page of Tasks");
    Page<TaskExecutorDto> page = taskService.findAll(pageable);
    HttpHeaders headers = PaginationUtil
        .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /tasks/:id} : get the "id" task.
   *
   * @param id the id of the taskDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
   *         the taskDto, or with status {@code 404 (Not Found)}.
   */
  @Operation(
      summary = "searches task by 'id'", description = "searches task by 'id'", tags = { "task" })
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200", description = "search results matching criteria",
              content = @Content(schema = @Schema(implementation = TaskExecutorDto.class))),
          @ApiResponse(responseCode = "400", description = "bad input parameter"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @GetMapping("/tasks/{id}")
  public ResponseEntity<TaskExecutorDto> getTask(@PathVariable Long id) {
    log.debug("REST request to get Task : {}", id);
    Optional<TaskExecutorDto> taskDto = taskService.findOne(id);
    return ResponseUtil.wrapOrNotFound(ENTITY_NAME, taskDto);
  }

  /**
   * {@code DELETE  /tasks} : Deletes an existing task.
   *
   * @param id the id of the taskDto to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} or with
   *         status {@code 400 (Bad Request)} if the taskDto is not valid, or with
   *         status {@code 500 (Internal Server Error)} if the taskDto couldn't be
   *         deleted.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @Operation(
      summary = "delete Task", description = "delete an Task to the system", tags = { "task" })
  @ApiResponses(
      value = { @ApiResponse(responseCode = "204", description = "Task deleted"),
          @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
          @ApiResponse(responseCode = "404", description = "not found") })
  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) throws URISyntaxException {
    log.debug("REST request to delete Task : {}", id);
    taskService.delete(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
  }
}
