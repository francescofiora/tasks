package it.francescofiora.tasks.taskapi.web.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.francescofiora.tasks.message.enumeration.TaskStatus;
import it.francescofiora.tasks.message.enumeration.TaskType;
import it.francescofiora.tasks.taskapi.service.TaskService;
import it.francescofiora.tasks.taskapi.service.dto.NewTaskDto;
import it.francescofiora.tasks.taskapi.service.dto.TaskDto;
import it.francescofiora.tasks.taskapi.service.dto.UpdatableTaskDto;
import it.francescofiora.tasks.taskapi.web.errors.BadRequestAlertException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Tasks Api.
 */
@RestController
@Tag(name = "task", description = "Task Rest API")
@RequestMapping("/api/v1")
public class TasksApi extends AbstractApi {

  private static final String ENTITY_NAME = "TaskDto";
  private static final String TAG = "task";

  private final TaskService taskService;

  public TasksApi(TaskService taskService) {
    super(ENTITY_NAME);
    this.taskService = taskService;
  }

  /**
   * {@code POST  /tasks} : Create a new task.
   *
   * @param taskDto the task to create
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Add new Task", description = "Add a new Task to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Task created"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid")})
  @PostMapping("/tasks")
  public ResponseEntity<Void> createTask(
      @Parameter(description = "Add new Task") @Valid @RequestBody NewTaskDto taskDto) {
    var result = taskService.create(taskDto);
    return postResponse("/api/v1/tasks/" + result.getId(), result.getId());
  }

  /**
   * {@code PATCH  /tasks:id} : Patches an existing task.
   *
   * @param taskDto the task to patch
   * @param id the id of the author to update
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "Patch Task", description = "Patch an Task to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Task patched"),
      @ApiResponse(responseCode = "400", description = "Invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "Not found")})
  @PatchMapping("/tasks/{id}")
  public ResponseEntity<Void> patchTask(
      @Parameter(description = "Task to update") @Valid @RequestBody UpdatableTaskDto taskDto,
      @Parameter(description = "The id of the task to update", required = true,
          example = "1") @PathVariable("id") Long id) {
    if (!id.equals(taskDto.getId())) {
      throw new BadRequestAlertException(ENTITY_NAME, String.valueOf(taskDto.getId()),
          "Invalid id");
    }
    taskService.patch(taskDto);
    return patchResponse(id);
  }

  /**
   * {@code GET  /tasks} : get all the tasks.
   *
   * @param description the description
   * @param type the type of the task
   * @param status the status of the task
   * @param pageable the pagination information
   * @return the {@link ResponseEntity} with the list of tasks
   */
  @Operation(summary = "Searches tasks",
      description = "By passing in the appropriate options, "
          + "you can search for available tasks in the system",
      tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = TaskDto.class)))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter")})
  @GetMapping("/tasks")
  public ResponseEntity<List<TaskDto>> getAllTasks(
      @Parameter(description = "Description", example = "description",
          in = ParameterIn.QUERY) @RequestParam(required = false) String description,
      @Parameter(description = "Type of task", example = "SHORT",
          in = ParameterIn.QUERY) @RequestParam(required = false) TaskType type,
      @Parameter(description = "Status of task", example = "TERMINATED",
          in = ParameterIn.QUERY) @RequestParam(required = false) TaskStatus status,
      @Parameter(example = "{\n  \"page\": 0,  \"size\": 10}",
          in = ParameterIn.QUERY) Pageable pageable) {
    return getResponse(taskService.findAll(description, type, status, pageable));
  }

  /**
   * {@code GET  /tasks/:id} : get the "id" task.
   *
   * @param id the id of the taskDto to retrieve
   * @return the {@link ResponseEntity} with the task
   */
  @Operation(summary = "Searches task by 'id'", description = "Searches task by 'id'", tags = {TAG})
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results matching criteria",
          content = @Content(schema = @Schema(implementation = TaskDto.class))),
      @ApiResponse(responseCode = "400", description = "Bad input parameter"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @GetMapping("/tasks/{id}")
  public ResponseEntity<TaskDto> getTask(@Parameter(description = "id of the task to get",
      required = true, example = "1") @PathVariable Long id) {
    return getResponse(taskService.findOne(id), id);
  }

  /**
   * {@code DELETE  /tasks} : Deletes an existing task.
   *
   * @param id the id of the task to retrieve
   * @return the {@link ResponseEntity}
   */
  @Operation(summary = "delete Task", description = "delete an Task to the system", tags = {TAG})
  @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Task deleted"),
      @ApiResponse(responseCode = "400", description = "invalid input, object invalid"),
      @ApiResponse(responseCode = "404", description = "not found")})
  @DeleteMapping("/tasks/{id}")
  public ResponseEntity<Void> deleteTask(@Parameter(description = "id of the task to delete",
      required = true, example = "1") @PathVariable Long id) {
    taskService.delete(id);
    return deleteResponse(id);
  }
}
