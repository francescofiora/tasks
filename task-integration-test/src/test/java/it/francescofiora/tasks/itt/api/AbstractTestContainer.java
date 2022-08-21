package it.francescofiora.tasks.itt.api;

import it.francescofiora.tasks.itt.SpringAplicationContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;

/**
 * Abstract class for Test Containers.
 */
public class AbstractTestContainer {

  private static Network network = Network.newNetwork();

  protected static MySQLContainer<?> createMySqlContainer() {
    // @formatter:off
    var mysql = new MySQLContainer<>("mysql:8.0.27")
        .withNetwork(network)
        .withNetworkAliases("tasks-mysql")
        .withUsername("japp").withPassword("secret").withDatabaseName("tasks");
    // @formatter:on
    return mysql;
  }

  protected static GenericContainer<?> createArtemisContainer() {
    // @formatter:off
    var artemis = new GenericContainer<>("artemis-debian")
        .withNetwork(network)
        .withNetworkAliases("tasks-activemq")
        .withEnv("ARTEMIS_USER", "artemis").withEnv("ARTEMIS_PASSWORD", "artemis")
        .withEnv("ANONYMOUS_LOGIN", "false")
        .withExposedPorts(61616, 8161);
    // @formatter:on
    return artemis;
  }

  protected static GenericContainer<?> createMongoDbContainer() {
    // @formatter:off
    var mongo = new GenericContainer<>("mongo:5.0.3")
        .withNetwork(network)
        .withNetworkAliases("tasks-mongodb")
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "secret")
        .withExposedPorts(27017);
    // @formatter:on
    return mongo;
  }

  protected static SpringAplicationContainer createSpringAplicationContainer(
      String dockerImageName) {
    return new SpringAplicationContainer(dockerImageName)
        .withNetwork(network);
  }

  protected static GenericContainer<?> createGenericContainer(String dockerImageName) {
    var container = new GenericContainer<>(dockerImageName).withNetwork(network);
    return container;
  }

  protected static void execInContainer(GenericContainer<?> container, String... command) {
    try {
      System.out.println();
      for (var str : command) {
        System.out.print(str + " ");
      }
      System.out.println();
      var result = container.execInContainer(command);
      System.out.println(result.getStdout());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
