package it.francescofiora.tasks.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescofiora.tasks.itt.container.SpringAplicationContainer;
import it.francescofiora.tasks.itt.container.StartStopContainers;
import it.francescofiora.tasks.itt.util.ContainerGenerator;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

@Slf4j
class ApiSslTest extends AbstractTestContainer {

  private static final String BROKER_URL = "ssl://tasks-activemq:61616";
  private static final String DATASOURCE_URL = "jdbc:mysql://tasks-mysql:3306/tasks?"
      + "verifyServerCertificate=true&useSSL=true&requireSSL=true"
      + "&clientCertificateKeyStoreUrl=file:./config/tasks-executor-keystore.jks"
      + "&clientCertificateKeyStorePassword=mypass"
      + "&trustCertificateKeyStoreUrl=file:./config/truststore.ts"
      + "&trustCertificateKeyStorePassword=mypass";
  private static final String MONGODB_URI = "mongodb://root:secret@tasks-mongodb:27017/?ssl=true";

  private static MySQLContainer<?> mySqlContainer;
  private static GenericContainer<?> myMongoDBContainer;
  private static GenericContainer<?> myArtemis;
  private static SpringAplicationContainer taskExecutor;
  private static SpringAplicationContainer taskApi;

  private static StartStopContainers containers = new StartStopContainers();
  private static ContainerGenerator containerGenerator = new ContainerGenerator(true);

  private static RestTemplate getRestTemplateSsl() throws Exception {
    TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
    var sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
    var csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    var restTemplate = new RestTemplate();
    restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(
        HttpClientBuilder.create().setSSLSocketFactory(csf).build()));
    return restTemplate;
  }

  @BeforeAll
  public static void init() throws Exception {
    containerGenerator.useSsl();

    mySqlContainer = containerGenerator.createMySqlContainer();
    containers.add(mySqlContainer);

    myArtemis = containerGenerator.createArtemisContainer();
    containers.add(myArtemis);

    myMongoDBContainer = containerGenerator.createMongoDbContainer();
    containers.add(myMongoDBContainer);

    var tmpDir = containerGenerator.getTmpDir();

    // @formatter:off
    taskExecutor = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-executor")
        .withEnv("SPRING_PROFILES_ACTIVE", "AmqSsl,Logging")
        .withEnv("SSL_ENABLED", "true")
        .withEnv("KEYSTORE_PASSWORD", "mypass")
        .withEnv("KEYSTORE_FILE", "/workspace/config/tasks-executor-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", "mypass")
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("BROKER_USER", "tasksexecutor")
        .withUsername(USER)
        .withPassword(PASSWORD)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8082)
        .withRestTemplate(getRestTemplateSsl());
    // @formatter:on
    taskExecutor.addFileSystemBind(tmpDir + File.separator + "tasks-executor-keystore.jks",
        "/workspace/config/tasks-executor-keystore.jks", BindMode.READ_ONLY);
    taskExecutor.addFileSystemBind(tmpDir + File.separator + "truststore.ts",
        "/workspace/config/truststore.ts", BindMode.READ_ONLY);
    containers.add(taskExecutor);

    // @formatter:off
    taskApi = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-api")
        .withEnv("SPRING_PROFILES_ACTIVE", "MongoSsl,AmqSsl,Logging")
        .withEnv("SSL_ENABLED", "true")
        .withEnv("KEYSTORE_PASSWORD", "mypass")
        .withEnv("KEYSTORE_FILE", "/workspace/config/tasks-api-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", "mypass")
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withEnv("MONGODB_URI", MONGODB_URI)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("BROKER_USER", "tasksapi")
        .withUsername(USER)
        .withPassword(PASSWORD)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8081)
        .withRestTemplate(getRestTemplateSsl());
    // @formatter:on
    taskApi.addFileSystemBind(tmpDir + File.separator + "tasks-api-keystore.jks",
        "/workspace/config/tasks-api-keystore.jks", BindMode.READ_ONLY);
    taskApi.addFileSystemBind(tmpDir + File.separator + "truststore.ts",
        "/workspace/config/truststore.ts", BindMode.READ_ONLY);
    containers.add(taskApi);
  }

  @Test
  void testHealth() throws Exception {
    assertTrue(containers.areRunning());

    var result = taskApi.performGet(HEALTH_URI);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).contains("UP");

    result = taskExecutor.performGet(HEALTH_URI);
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(result.getBody()).contains("UP");
  }

  @Test
  void test() throws Exception {
    Map<Long, String> tasks = new HashMap<>();

    for (var i = 0; i < 10; i++) {
      var description = "description" + i;
      var json = "{ \"description\": \"" + description + "\", " + " \"type\": \"SHORT\", "
          + "\"parameters\": [ { \"name\": \"name" + i + "\",  \"value\": \"value\"  } ] }";

      var id = taskApi.createAndReturnId(TASKS_API_URI, json);
      log.debug("id = " + id);
      tasks.put(id, description);
    }

    var result = taskApi.performGet(TASKS_API_URI, Pageable.ofSize(1));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    for (var i = 0; i < 10; i++) {
      assertThat(result.getBody()).contains("\"description" + i + "\"");
    }

    var chek = new HashSet<>(tasks.keySet());
    var max = 50;

    while (!chek.isEmpty() && max > 0) {
      max--;
      for (Iterator<Long> itr = chek.iterator(); itr.hasNext();) {
        var id = itr.next();
        var tasksIdUri = String.format(TASKS_API_ID_URI, id);
        result = taskApi.performGet(tasksIdUri);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).contains("\"" + tasks.get(id) + "\"");

        if (!result.getBody().contains("\"status\":\"SCHEDULATED\"")) {
          itr.remove();
        }
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }

    result = taskExecutor.performGet(TASKS_EXECUTOR_URI, Pageable.ofSize(1));
    assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    log.debug("Body = " + result.getBody());

    for (var entry : tasks.entrySet()) {
      var tasksIdUri = String.format(TASKS_EXECUTOR_ID_URI, entry.getKey());
      result = taskExecutor.performGet(tasksIdUri);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(result.getBody())
          .contains("\"name\":\"taskRef\",\"value\":\"" + entry.getKey() + "\"");
      assertThat(result.getBody()).contains("\"status\":\"TERMINATED\"");
    }
  }

  @AfterAll
  public static void endAll() {
    containers.stopAndCloseAll();
    containerGenerator.endUseSsl();
  }
}
