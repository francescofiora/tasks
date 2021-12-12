package it.francescofiora.tasks.taskapi.web.util;

import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * Header Util.
 */
public interface HeaderUtil {

  String X_ALERT = "X-alert";
  String X_ERROR = "X-error";
  String X_PARAMS = "X-params";

  /**
   * Create Entity Creation Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  static HttpHeaders createEntityCreationAlert(String entityName, String param) {
    return createAlert(entityName + ".created", param);
  }

  /**
   * Create Entity Get Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  static HttpHeaders createEntityGetAlert(String entityName, String param) {
    return createAlert(entityName + ".get", param);
  }

  /**
   * Create Entity Patch Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  static HttpHeaders createEntityPatchAlert(String entityName, String param) {
    return createAlert(entityName + ".patched", param);
  }

  /**
   * Create Entity Deletion Alert.
   *
   * @param entityName the entity name
   * @param param the parameter
   * @return HttpHeaders
   */
  static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
    return createAlert(entityName + ".deleted", param);
  }

  /**
   * Create Failure Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  static HttpHeaders createFailureAlert(String alert, String param, String errorMessage) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.add(X_PARAMS, param);
    headers.add(X_ERROR, errorMessage);
    return headers;
  }

  /**
   * Create Failure Alert.
   *
   * @param alert the message
   * @param params the list of parameters
   * @param errorMessage the error message
   * @return HttpHeaders
   */
  static HttpHeaders createFailureAlert(String alert, List<String> params, String errorMessage) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.addAll(X_PARAMS, params);
    headers.add(X_ERROR, errorMessage);
    return headers;
  }

  /**
   * Create Alert.
   *
   * @param alert the message
   * @param param the parameter
   * @return HttpHeaders
   */
  static HttpHeaders createAlert(String alert, String param) {
    var headers = new HttpHeaders();
    headers.add(X_ALERT, alert);
    headers.add(X_PARAMS, param);
    return headers;
  }
}
