package it.francescofiora.tasks.taskapi.config;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskapi.config.parameter.DbProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class DatabaseSslConfigTest {

  private static final String DB_NAME = "tasks";

  @Autowired
  private DatabaseSslConfig databaseSslConfig;

  @Test
  void getDatabaseNameTest() {
    assertThat(databaseSslConfig.getDatabaseName()).isEqualTo(DB_NAME);
  }

  @TestConfiguration
  static class TestContextConfiguration {

    @Bean
    public DbProperties dbProperties() {
      var db = new DbProperties();
      db.setDatabase(DB_NAME);
      db.setKeystorefile("Keystorefile");
      db.setKeystorepassword("Keystorepassword");
      db.setTruststorefile("Truststorefile");
      db.setTruststorepassword("Truststorepassword");
      db.setUri("mongodb://root:secret@localhost:27017");
      return db;
    }

    @Bean
    public DatabaseSslConfig databaseSslConfig() {
      return new DatabaseSslConfig();
    }
  }
}
