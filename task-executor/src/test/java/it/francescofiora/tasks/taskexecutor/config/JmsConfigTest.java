package it.francescofiora.tasks.taskexecutor.config;

import static org.assertj.core.api.Assertions.assertThat;

import it.francescofiora.tasks.taskexecutor.config.parameter.JmsProperties;
import it.francescofiora.tasks.taskexecutor.config.parameter.SslProperties;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.junit.jupiter.api.Test;

/**
 * JmsConfig Test.
 */
public class JmsConfigTest {

  @Test
  void testConnectionFactory() throws Exception {
    var properties = new JmsProperties();
    properties.setBrokerUrl("tcp://localhost:61616");
    properties.setUser("user");
    var ssl = new SslProperties();
    ssl.setKeyStorePass("KeyStorePass");
    ssl.setKeyStorePath("KeyStorePath");
    ssl.setTrustStorePass("TrustStorePass");
    ssl.setTrustStorePath("TrustStorePath");
    properties.setSsl(ssl);
    var config = new JmsConfig();
    var connectionFactory = (ActiveMQSslConnectionFactory) config.connectionFactory(properties);
    assertThat(connectionFactory.getBrokerURL()).isEqualTo(properties.getBrokerUrl());
    assertThat(connectionFactory.getUserName()).isEqualTo(properties.getUser());
    assertThat(connectionFactory.getTrustStore())
        .isEqualTo(properties.getSsl().getTrustStorePath());
    assertThat(connectionFactory.getTrustStorePassword())
        .isEqualTo(properties.getSsl().getTrustStorePass());
    assertThat(connectionFactory.getKeyStore()).isEqualTo(properties.getSsl().getKeyStorePath());
    assertThat(connectionFactory.getKeyStorePassword())
        .isEqualTo(properties.getSsl().getKeyStorePass());
  }
}
