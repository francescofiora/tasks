package it.francescofiora.tasks.taskapi.endtoend;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.web.api.AbstractApi;
import it.francescofiora.tasks.taskapi.web.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Abstract Test class for EndToEnd tests.
 */
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
    return "http://localhost:" + randomServerPort + path;
  }

  protected void testUnauthorized(String path) {
    var result = unauthorizedGet(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

    result = unauthorizedGetWrongUser(path, String.class);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }

  protected <T> ResponseEntity<T> unauthorizedGet(String path, Class<T> responseType) {
    return restTemplate.getForEntity(AbstractApi.createUri(getPath(path)), responseType);
  }

  protected <T> ResponseEntity<T> unauthorizedGetWrongUser(String path, Class<T> responseType) {
    var headers = new HttpHeaders();
    headers.setBasicAuth("wrong_user", "wrong_password");
    var request = new HttpEntity<>(headers);
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String path, Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.GET, request, responseType);
  }

  protected <T> ResponseEntity<T> performGet(String path, MultiValueMap<String, String> pageable,
      Class<T> responseType) {
    var request = new HttpEntity<>(createHttpHeaders());
    var uri = UriComponentsBuilder.fromHttpUrl(getPath(path)).queryParams(pageable).build();
    return restTemplate.exchange(uri.toUriString(), HttpMethod.GET, request, responseType);
  }

  protected ResponseEntity<Void> performDelete(String path) {
    var request = new HttpEntity<>(createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.DELETE, request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPost(String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.postForEntity(AbstractApi.createUri(getPath(path)), request, Void.class);
  }

  protected <T> ResponseEntity<Void> performPatch(String path, T body) {
    var request = new HttpEntity<>(body, createHttpHeaders());
    return restTemplate.exchange(getPath(path), HttpMethod.PATCH, request, Void.class);
  }

  private void checkHeaders(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> Long createAndReturnId(String path, T body, String alert) {
    var result = performPost(path, body);
    assertThat(result.getHeaders()).containsKeys(HeaderUtil.X_ALERT, HttpHeaders.LOCATION,
        HeaderUtil.X_PARAMS);
    assertThat(result.getHeaders().get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(result.getHeaders().get(HeaderUtil.X_PARAMS)).isNotEmpty();
    var id = getIdFormHttpHeaders(result.getHeaders());
    checkHeaders(result.getHeaders(), alert, String.valueOf(id));
    return id;
  }

  protected <T> void patch(String path, T body, String alert, String param) {
    var result = performPatch(path, body);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  protected <T> void assertCreateBadRequest(String path, T body, String alert, String param) {
    var result = performPost(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected <T> void assertPatchBadRequest(String path, T body, String alert, String param) {
    var result = performPatch(path, body);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private void checkHeadersError(HttpHeaders headers, String alert, String param) {
    assertThat(headers).containsKeys(HeaderUtil.X_ALERT, HeaderUtil.X_ERROR, HeaderUtil.X_PARAMS);
    assertThat(headers.get(HeaderUtil.X_ALERT)).contains(alert);
    assertThat(headers.get(HeaderUtil.X_ERROR)).isNotEmpty();
    assertThat(headers.get(HeaderUtil.X_PARAMS)).contains(param);
  }

  protected <T> T get(String path, Class<T> responseType, String alert, String param) {
    var result = performGet(path, responseType);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    checkHeaders(result.getHeaders(), alert, param);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> T get(String path, MultiValueMap<String, String> pageable, Class<T> responseType,
      String alert, String param) {
    var result = performGet(path, pageable, responseType);
    checkHeaders(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    var value = result.getBody();
    assertThat(value).isNotNull();
    return value;
  }

  protected <T> void assertGetNotFound(String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetNotFound(String path, MultiValueMap<String, String> pageable,
      Class<T> responseType, String alert, String param) {
    var result = performGet(path, pageable, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  protected <T> void assertGetBadRequest(String path, Class<T> responseType, String alert,
      String param) {
    var result = performGet(path, responseType);
    checkHeadersError(result.getHeaders(), alert, param);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  protected void delete(String path, String alert, String param) {
    var result = performDelete(path);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    checkHeaders(result.getHeaders(), alert, param);
  }

  protected Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  private HttpHeaders createHttpHeaders() {
    var headers = new HttpHeaders();
    headers.setBasicAuth(user, password);
    return headers;
  }
}
