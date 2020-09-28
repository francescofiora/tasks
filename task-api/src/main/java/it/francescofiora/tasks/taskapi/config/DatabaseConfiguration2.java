//package it.francescofiora.tasks.taskapi.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings.Builder;
//
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.security.KeyStore;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//import javax.net.ssl.KeyManagerFactory;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.TrustManagerFactory;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.ssl.SSLContextBuilder;
////import org.apache.http.ssl.TrustStrategy;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@Profile("!dev")
//@EnableMongoRepositories("it.francescofiora.tasks.taskapi.repository")
//public class DatabaseConfiguration2 extends AbstractMongoClientConfiguration {
//
//  private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
//
//  @Value("${spring.data.mongodb.database}")
//  private String database;
//
//  @Value("${spring.data.mongodb.keystorefile}")
//  private String keystoreFile;
//
//  @Value("${spring.data.mongodb.keystorepassword}")
//  private String keyPassword;
//
//  @Value("${spring.data.mongodb.truststorefile}")
//  private String truststoreFile;
//
//  @Value("${spring.data.mongodb.truststorepassword}")
//  private String truststorepassword;
//
//  @Value("${spring.data.mongodb.uri}")
//  private String uri;
//
//  @Override
//  protected String getDatabaseName() {
//    return database;
//  }
//
//  @Override
//  protected void configureClientSettings(Builder builder) {
//    builder.applyConnectionString(new ConnectionString(uri))
//        .applyToSslSettings(b -> b.enabled(true).context(getSslContext()));
//  }
//
//  private SSLContext getSslContext() {
//    try {
//      KeyStore keystore = KeyStore.getInstance("jks");
//      try (InputStream in = new FileInputStream(keystoreFile)) {
//        keystore.load(in, keyPassword.toCharArray());
//        log.info("load " + keystoreFile);
//      }
//
//      KeyManagerFactory keyManagerFactory = KeyManagerFactory
//          .getInstance("SunX509");
//      keyManagerFactory.init(keystore, keyPassword.toCharArray());
//
//      KeyStore truststore = KeyStore.getInstance("jks");
//      try (InputStream in = new FileInputStream(truststoreFile)) {
//        keystore.load(in, truststorepassword.toCharArray());
//        log.info("load " + truststoreFile);
//      }
//            
////      SSLContext sslContext = SSLContextBuilder.create()
////          .loadTrustMaterial(null, (a, b) -> true)
////          .loadKeyMaterial(keystore, keyPassword.toCharArray())
////          .build();
//
////      SSLContext sslContext = SSLContextBuilder.create()
////          .loadTrustMaterial(truststore, null)
////          .loadKeyMaterial(keystore, keyPassword.toCharArray())
////          .build();
//      
//      TrustManagerFactory trustManagerFactory = TrustManagerFactory
//          .getInstance("SunX509");
//      trustManagerFactory.init(truststore);
////      log.info("trustManagerFactory 1: " + trustManagerFactory.getAlgorithm());
////      log.info("trustManagerFactory 2: " + trustManagerFactory.getProvider());
////      TrustManager[] ts = trustManagerFactory.getTrustManagers();
////      log.info("TrustManager length: " + ts.length);
////      for (TrustManager myTm : ts) {
////        log.info("TrustManager: " + myTm);
////      }
////      sun.security.ssl.X509TrustManagerImpl
//
//      TrustManager tm = new X509TrustManager() {
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] chain, String authType)
//            throws CertificateException {
//          log.info("checkClientTrusted - authType: " + authType);
//          if (chain != null && chain.length > 0) {
//            for (X509Certificate x509 : chain) {
//              log.info("" + x509);
//            }
//          }
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] chain, String authType)
//            throws CertificateException {
//          log.info("checkServerTrusted - authType: " + authType);
//          if (chain != null && chain.length > 0) {
//            for (X509Certificate x509 : chain) {
//              log.info("" + x509);
//            }
//          }
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//          return null;
//        }
//
//      };
//
//      SSLContext sslContext = SSLContext.getInstance("SSL");
////      sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { tm },
////          new SecureRandom());
//
//      sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
//          new SecureRandom());
//      
//      return sslContext;
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException(e);
//    }
//  }
//}
