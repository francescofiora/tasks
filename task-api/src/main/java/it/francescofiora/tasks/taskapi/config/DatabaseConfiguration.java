package it.francescofiora.tasks.taskapi.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings.Builder;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@Profile("!dev")
@EnableMongoRepositories("it.francescofiora.tasks.taskapi.repository")
public class DatabaseConfiguration extends AbstractMongoClientConfiguration {

  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @Value("${spring.data.mongodb.database}")
  private String database;

  @Value("${spring.data.mongodb.keystorefile}")
  private String keystoreFile;

  @Value("${spring.data.mongodb.keystorepassword}")
  private String keyPassword;

  @Value("${spring.data.mongodb.truststorefile}")
  private String truststoreFile;

  @Value("${spring.data.mongodb.truststorepassword}")
  private String truststorepassword;

  @Value("${spring.data.mongodb.uri}")
  private String uri;

  @Override
  protected String getDatabaseName() {
    return database;
  }

  @Override
  protected void configureClientSettings(Builder builder) {
    builder.applyConnectionString(new ConnectionString(uri))
        .applyToSslSettings(b -> b.enabled(true).context(getSslContext()));
  }

  private SSLContext getSslContext() {
    try {
      KeyStore keystore = KeyStore.getInstance("jks");
      try (InputStream in = new FileInputStream(keystoreFile)) {
        keystore.load(in, keyPassword.toCharArray());
      }

      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keystore, keyPassword.toCharArray());

      KeyStore truststore = KeyStore.getInstance("jks");
      try (InputStream in = new FileInputStream(truststoreFile)) {
        keystore.load(in, truststorepassword.toCharArray());
      }

      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
      trustManagerFactory.init(truststore);

      TrustManager tm = new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

      };

      SSLContext sslContext = SSLContext.getInstance("SSL");
      sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { tm },
          new SecureRandom());

      // TODO fix later
//      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
//          new SecureRandom());

      return sslContext;
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
