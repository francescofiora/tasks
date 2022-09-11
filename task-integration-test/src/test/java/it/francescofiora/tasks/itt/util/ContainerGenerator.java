package it.francescofiora.tasks.itt.util;

import it.francescofiora.tasks.itt.container.SpringAplicationContainer;
import it.francescofiora.tasks.itt.ssl.CertificateGenerator;
import java.io.File;
import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.shaded.com.google.common.io.Files;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

/**
 * Container Generator.
 */
@RequiredArgsConstructor
public class ContainerGenerator {

  private Network network = Network.newNetwork();
  private CertificateGenerator generator;
  private final boolean useSsl;

  public static final String MYSQL_USER_ADMIN = "japp";
  public static final String MYSQL_PASSWORD_ADMIN = "secret";

  public static final String TASKS_MYSQL = "tasks-mysql";
  public static final String TASKS_MONGODB = "tasks-mongodb";
  public static final String TASKS_ACTIVEMQ = "tasks-activemq";
  public static final String TASKS_EXECUTOR = "tasks-executor";
  public static final String TASKS_API = "tasks-api";

  @Getter
  private String tmpDir = "";

  /**
   * Use Ssl.
   *
   * @throws Exception if errors occur
   */
  public void useSsl() throws Exception {
    if (!useSsl) {
      throw new Exception("useSsl is false");
    }

    tmpDir = UtilResource.getResourceFile("ssl");
    generator = new CertificateGenerator(tmpDir, "ca.francescofiora.it");
    generator.clean();
    generator.generateRoot();

    generator.generateSignedCertificate(TASKS_ACTIVEMQ, "01", true);
    generator.generateSignedCertificate(TASKS_MYSQL, "02", false);
    generator.generateSignedCertificate(TASKS_MONGODB, "03", false);
    generator.generateSignedCertificate(TASKS_EXECUTOR, "04", true);
    generator.generateSignedCertificate(TASKS_API, "05", true);
    generator.appendCertificate(TASKS_MONGODB);

    var fromPath = generator.getFullPath(CertificateGenerator.TRUSTSTORE);
    var toPath = UtilResource.getResourceFile("artemis") + File.separator + "etcextra"
        + File.separator + CertificateGenerator.TRUSTSTORE;
    Files.copy(new File(fromPath), new File(toPath));

    var name = String.format(CertificateGenerator.KEY_STORE_JKS_FILE, TASKS_ACTIVEMQ);
    fromPath = generator.getFullPath(name);
    toPath = UtilResource.getResourceFile("artemis") + File.separator + "etcextra" + File.separator
        + name;
    Files.copy(new File(fromPath), new File(toPath));
  }

  /**
   * Create MySql Container.
   *
   * @return MySQLContainer
   */
  public MySQLContainer<?> createMySqlContainer() {
    // @formatter:off
    var mysql = new MySQLContainer<>("mysql:8.0.27")
        .withNetwork(network)
        .withNetworkAliases(TASKS_MYSQL)
        .withUsername(MYSQL_USER_ADMIN).withPassword(MYSQL_PASSWORD_ADMIN)
        .withDatabaseName("tasks");
    // @formatter:on
    if (useSsl) {
      mysql.withUrlParam("useSSL", "true");
      mysql.addFileSystemBind(UtilResource.getResourceFile("mysqld.cnf"),
          "/etc/mysql/conf.d/mysqld.cnf", BindMode.READ_ONLY);
      mysql.addFileSystemBind(tmpDir, "/etc/certs", BindMode.READ_ONLY);
    }

    return mysql;
  }

  /**
   * Create Artemis Container.
   *
   * @return GenericContainer
   */
  public GenericContainer<?> createArtemisContainer() {
    // @formatter:off
    var artemis = new GenericContainer<>("artemis-debian")
        .withNetwork(network)
        .withNetworkAliases(TASKS_ACTIVEMQ)
        .withEnv("ARTEMIS_USER", "artemis").withEnv("ARTEMIS_PASSWORD", "artemis")
        .withEnv("ANONYMOUS_LOGIN", "false")
        .withEnv("EXTRA_ARGS", "--http-host tasks-activemq --relax-jolokia")
        .withExposedPorts(61616, 8161);
    // @formatter:on
    if (useSsl) {
      var artemisInstance = UtilResource.getResourceFile("artemis");
      artemis.addFileSystemBind(artemisInstance, "/var/lib/artemis-instance", BindMode.READ_WRITE);
      artemis.start();
      artemis.stop();
      try {
        FileUtils.copyDirectory(new File(artemisInstance + File.separator + "etcextra"),
            new File(artemisInstance + File.separator + "etc"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return artemis;
  }

  /**
   * Create MongoDb Container.
   *
   * @return GenericContainer
   */
  public GenericContainer<?> createMongoDbContainer() {
    // @formatter:off
    var mongo = new GenericContainer<>("mongo:5.0.3")
        .withNetwork(network)
        .withNetworkAliases(TASKS_MONGODB)
        .withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
        .withEnv("MONGO_INITDB_ROOT_PASSWORD", "secret")
        .withExposedPorts(27017);
    // @formatter:on
    if (useSsl) {
      mongo.withCommand("--config", "/etc/mongo.conf", "--bind_ip_all");
      mongo.addFileSystemBind(UtilResource.getResourceFile("mongo.conf"), "/etc/mongo.conf",
          BindMode.READ_ONLY);
      mongo.addFileSystemBind(tmpDir, "/etc/ssl", BindMode.READ_ONLY);
    }
    return mongo;
  }

  public SpringAplicationContainer createSpringAplicationContainer(String dockerImageName) {
    return new SpringAplicationContainer(dockerImageName).withNetwork(network);
  }

  public void endUseSsl() {
    generator.clean();
  }

}
