package it.francescofiora.tasks.taskapi.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.web.util.HeaderUtil;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AbstractTestEndToEnd {

  @LocalServerPort
  private int randomServerPort;

  @Autowired
  private TestRestTemplate restTemplate;

  @Value("${spring.security.user.name}")
  private String user;

  @Value("${spring.security.user.password}")
  private String password;

  private String getPath(String path) {
    return "http://localhost:" + randomServerPort + "/tasks-api" + path;
  }

  protected void testUnauthorized(String path) throws Exception {
    ResponseEntity<String> result = unauthorizedGet(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = unauthorizedGetWrongUser(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  protected <T> ResponseEntity<T> unauthorizedGet(String path, Class<T> responseType)
      throws Exception {
    return restTemplate.getForEntity(new URI(getPath(path)), responseType);
  }

  protected <T> ResponseEntity<T> unauthorizedGetWrongUser(String path, Class<T> responseType)
      throws Exception {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth("wrong_user", "wrong_password");
    HttpEntity<Void> request = new HttpEntity<>(null, headers);
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String path, Class<T> responseType) throws Exception {
    HttpEntity<Void> request = new HttpEntity<>(null, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String path, Pageable pageable, Class<T> responseType)
      throws Exception {
    HttpEntity<Pageable> request = new HttpEntity<>(pageable, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String path) throws Exception {
    HttpEntity<Void> request = new HttpEntity<>(null, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String path, T body) throws Exception {
    HttpEntity<T> request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.postForEntity(new URI(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPatch(String path, T body) throws Exception {
    HttpEntity<T> request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.PATCH, request, Void.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).isNotEmpty();
  }

  protected <T> Long createAndReturnId(String path, T body, String alert) throws Exception {
    ResponseEntity<Void> result = performPost(path, body);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    checkHeaders(result.getHeaders(), alert);
    return getIdFormHttpHeaders(result.getHeaders());
  }

  protected <T> void patch(String path, T body, String alert) throws Exception {
    ResponseEntity<Void> result = performPatch(path, body);
    checkHeaders(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertCreateBadRequest(String path, T body, String alert) throws Exception {
    ResponseEntity<Void> result = performPost(path, body);
    checkHeadersError(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private void checkHeadersError(HttpHeaders headers, String alert) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).isNotEmpty();
  }

  protected <T> T get(String path, Class<T> responseType, String alert) throws Exception {
    ResponseEntity<T> result = performGet(path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String path, Pageable pageable, Class<T> responseType, String alert)
      throws Exception {
    ResponseEntity<T> result = performGet(path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    T value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String path, Class<T> responseType, String alert)
      throws Exception {
    ResponseEntity<T> result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String path, Pageable pageable, Class<T> responseType,
      String alert) throws Exception {
    ResponseEntity<T> result = performGet(path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String path, Class<T> responseType, String alert)
      throws Exception {
    ResponseEntity<T> result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String path, String alert) throws Exception {
    ResponseEntity<Void> result = performDelete(path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert);
  }

  protected void assertDeleteBadRequest(String path, String alert) throws Exception {
    ResponseEntity<Void> result = performDelete(path);
    checkHeadersError(result.getHeaders(), alert);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    String url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(user, password);
    return headers;
  }
}
