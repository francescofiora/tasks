package it.francescofiora.tasks.taskexecutor.web.api;

import it.francescofiora.tasks.taskexecutor.web.errors.NotFoundAlertException;
import it.francescofiora.tasks.taskexecutor.web.util.HeaderUtil;
import it.francescofiora.tasks.taskexecutor.web.util.PaginationUtil;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

/**
 * Abstract Api RestController.
 */
public abstract class AbstractApi {

  private final String entityName;

  protected AbstractApi(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final Page<T> page) {
    return getResponse(entityName, page);
  }

  /**
   * Create a ResponseEntity of a pageable GET action.
   *
   * @param refEntityName the entity Name
   * @param <T> type of the response
   * @param page the pagination information
   * @return ResponseEntity
   */
  protected <T> ResponseEntity<List<T>> getResponse(final String refEntityName,
      final Page<T> page) {
    var headers = PaginationUtil.getHttpHeadersfromPagination(refEntityName, page);
    // @formatter:off
    return ResponseEntity
        .ok()
        .headers(headers)
        .body(page.getContent());
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a GET action with an OK status, or if it's empty, it returns a
   * ResponseEntity with NOT_FOUND.
   *
   * @param <T> type of the response
   * @param maybeResponse response to return if present
   * @return response containing {@code maybeResponse} if present or
   *         {@link org.springframework.http.HttpStatus#NOT_FOUND}
   */
  protected <T> ResponseEntity<T> getResponse(Optional<T> maybeResponse, Long id) {
    // @formatter:off
    return maybeResponse
        .map(response -> ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityGetAlert(entityName, String.valueOf(id)))
            .body(response))
        .orElseThrow(() -> new NotFoundAlertException(entityName, String.valueOf(id),
            entityName + " not found with id " + id));
    // @formatter:on
  }

  /**
   * Create a ResponseEntity of a DELETE action.
   *
   * @param id the id of the resource deleted
   * @return ResponseEntity
   */
  protected ResponseEntity<Void> deleteResponse(final Long id) {
    // @formatter:off
    return ResponseEntity
        .noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(entityName, String.valueOf(id)))
        .build();
    // @formatter:on
  }
}
