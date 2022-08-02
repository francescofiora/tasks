package it.francescofiora.tasks.itt;

import java.net.URI;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;

/**
 * Container of a SpringAplication.
 */
@Getter
public class SpringAplicationContainer extends GenericContainer<SpringAplicationContainer> {

  private String username;
  private String password;

  private RestTemplate rest = new RestTemplate();

  public SpringAplicationContainer(String dockerImageName) {
    super(dockerImageName);
  }

  public SpringAplicationContainer withUsername(final String username) {
    this.username = username;
    return self().withEnv("APP_USER", username);
  }

  public SpringAplicationContainer withPassword(final String password) {
    this.password = password;
    return self().withEnv("APP_PASSWORD", password);
  }

  public SpringAplicationContainer withRestTemplate(final RestTemplate rest) {
    this.rest = rest;
    return self();
  }

  /**
   * Create HttpHeaders.
   *
   * @return HttpHeaders
   */
  public HttpHeaders createHttpHeaders() {
    var headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBasicAuth(getUsername(), getPassword());
    return headers;
  }

  public String getHttpPath(String path) {
    return "http://" + getHost() + ":" + getFirstMappedPort() + path;
  }

  public Long createAndReturnId(String path, String jsonBody) throws Exception {
    var result = performPost(path, jsonBody);
    return getIdFormHttpHeaders(result.getHeaders());
  }

  private Long getIdFormHttpHeaders(HttpHeaders headers) {
    var url = headers.get(HttpHeaders.LOCATION).get(0);
    return Long.valueOf(url.substring(url.lastIndexOf('/') + 1));
  }

  public ResponseEntity<String> performGet(String path) throws Exception {
    var request = new HttpEntity<>(null, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.GET, request, String.class);
  }

  public ResponseEntity<String> performGet(String path, Pageable pageable) throws Exception {
    var request = new HttpEntity<>(pageable, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.GET, request, String.class);
  }

  public ResponseEntity<Void> performDelete(String path) throws Exception {
    var request = new HttpEntity<>(null, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.DELETE, request, Void.class);
  }

  public ResponseEntity<Void> performPost(String path, String jsonBody) throws Exception {
    var request = new HttpEntity<>(jsonBody, createHttpHeaders());
    return rest.postForEntity(new URI(getHttpPath(path)), request, Void.class);
  }

  public ResponseEntity<Void> performPatch(String path, String jsonBody) throws Exception {
    var request = new HttpEntity<>(jsonBody, createHttpHeaders());
    return rest.exchange(getHttpPath(path), HttpMethod.PATCH, request, Void.class);
  }
}
