package it.francescofiora.tasks.taskapi.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings.Builder;
import it.francescofiora.tasks.taskapi.config.parameter.DbProperties;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Database Configuration.
 */
@Slf4j
@Configuration
@Profile("!dev")
@EnableMongoRepositories("it.francescofiora.tasks.taskapi.repository")
public class DatabaseConfiguration extends AbstractMongoClientConfiguration {

  @Autowired
  private DbProperties dbProperties;


  @Override
  protected String getDatabaseName() {
    return dbProperties.getDatabase();
  }

  @Override
  protected void configureClientSettings(Builder builder) {
    builder.applyConnectionString(new ConnectionString(dbProperties.getUri()))
        .applyToSslSettings(b -> b.enabled(true).context(getSslContext()));
  }

  private SSLContext getSslContext() {
    try {
      var keystore = KeyStore.getInstance("jks");
      try (var in = new FileInputStream(dbProperties.getKeystorefile())) {
        keystore.load(in, dbProperties.getKeystorepassword().toCharArray());
      }

      var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keystore, dbProperties.getKeystorepassword().toCharArray());

      var truststore = KeyStore.getInstance("jks");
      try (var in = new FileInputStream(dbProperties.getTruststorefile())) {
        keystore.load(in, dbProperties.getTruststorepassword().toCharArray());
      }

      var trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
      trustManagerFactory.init(truststore);

      var tm = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

      };

      var sslContext = SSLContext.getInstance("SSL");
      sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] {tm},
          new SecureRandom());

      // TODO fix later
      // sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
      // new SecureRandom());

      return sslContext;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
