package it.francescofiora.tasks.itt.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import it.francescofiora.tasks.itt.container.SpringAplicationContainer;
import it.francescofiora.tasks.itt.container.StartStopContainers;
import it.francescofiora.tasks.itt.ssl.CertificateGenerator;
import it.francescofiora.tasks.itt.util.ContainerGenerator;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
  private static final String EUREKA_URI =
      "http://" + USER + ":" + PASSWORD + "@" + ContainerGenerator.TASKS_EUREKA + ":8761/eureka";

  private static SpringAplicationContainer taskExecutor;
  private static SpringAplicationContainer taskApi;
  private static SpringAplicationContainer eureka;

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

    var mySql = containerGenerator.createMySqlContainer();
    containers.add(mySql);

    var artemis = containerGenerator.createArtemisContainer();
    containers.add(artemis);

    var mongoDb = containerGenerator.createMongoDbContainer();
    containers.add(mongoDb);

    // @formatter:off
    eureka = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-eureka")
        .withEnv("EUREKA_SERVER", ContainerGenerator.TASKS_EUREKA)
        .withNetworkAliases(ContainerGenerator.TASKS_EUREKA)
        .withUsername(USER)
        .withPassword(PASSWORD)
        .withLogConsumer(new Slf4jLogConsumer(log))
        .withExposedPorts(8761);
    // @formatter:on
    containers.add(eureka);

    var tmpDir = containerGenerator.getTmpDir();

    // @formatter:off
    taskExecutor = containerGenerator
        .createSpringAplicationContainer("francescofiora-task-executor")
        .withEnv("SPRING_PROFILES_ACTIVE", "AmqSsl,Logging")
        .withEnv("SSL_ENABLED", "true")
        .withEnv("KEYSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("KEYSTORE_FILE", "/workspace/config/tasks-executor-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withEnv("DATASOURCE_URL", DATASOURCE_URL)
        .withEnv("DATASOURCE_ADMIN_USERNAME", ContainerGenerator.MYSQL_USER_ADMIN)
        .withEnv("DATASOURCE_ADMIN_PASSWORD", ContainerGenerator.MYSQL_PASSWORD_ADMIN)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("BROKER_USER", "tasksexecutor")
        .withEnv("EUREKA_URI", EUREKA_URI)
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
        .withEnv("KEYSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("KEYSTORE_FILE", "/workspace/config/tasks-api-keystore.jks")
        .withEnv("TRUSTSTORE_PASSWORD", CertificateGenerator.PASSWORD)
        .withEnv("TRUSTSTORE_FILE", "/workspace/config/truststore.ts")
        .withEnv("MONGODB_URI", MONGODB_URI)
        .withEnv("BROKER_URL", BROKER_URL)
        .withEnv("BROKER_USER", "tasksapi")
        .withEnv("EUREKA_URI", EUREKA_URI)
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
    assertUp(result);

    result = taskExecutor.performGet(HEALTH_URI);
    assertUp(result);

    result = eureka.performGet(HEALTH_URI);
    assertUp(result);
  }

  @Test
  void testEureka() throws Exception {
    Set<String> chek = new HashSet<>();
    chek.add(taskApi.getContainerInfo().getNetworkSettings().getNetworks().values().iterator()
        .next().getIpAddress());
    chek.add(taskExecutor.getContainerInfo().getNetworkSettings().getNetworks().values().iterator()
        .next().getIpAddress());
    var max = 50;
    while (!chek.isEmpty() && max > 0) {
      max--;
      var result = eureka.performGet(EUREKA_APPS);
      assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
      for (Iterator<String> itr = chek.iterator(); itr.hasNext();) {
        if (result.getBody().contains(itr.next())) {
          itr.remove();
        }
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          log.error(e.getMessage());
        }
      }
    }
    assertThat(chek).isEmpty();
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
