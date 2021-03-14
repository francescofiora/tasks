package it.francescofiora.tasks.taskexecutor.config;

import it.francescofiora.tasks.message.MessageDto;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
@EnableJms
public class JmsConfig {

  /**
   * MessageConverter bean.
   * 
   * @return MessageConverter
   */
  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName(MessageDto.class.getName());
    return converter;
  }

  /**
   * create a connectionFactory.
   * 
   * @param brokerUrl      String
   * @param trustStorePath String
   * @param trustStorePass String
   * @param keyStorePath   String
   * @param keyStorePass   String
   * @param userName       String
   * @return ConnectionFactory
   * @throws Exception if truststore/keystore path is wrong
   */
  @Profile("!dev")
  @Bean
  public ConnectionFactory connectionFactory(
      @Value("${spring.activemq.broker-url}") String brokerUrl,
      @Value("${spring.activemq.ssl.trustStorePath}") String trustStorePath,
      @Value("${spring.activemq.ssl.trustStorePass}") String trustStorePass,
      @Value("${spring.activemq.ssl.keyStorePath}") String keyStorePath,
      @Value("${spring.activemq.ssl.keyStorePass}") String keyStorePass,
      @Value("${spring.activemq.user}") String userName) throws Exception {
    ActiveMQSslConnectionFactory factory = new ActiveMQSslConnectionFactory(brokerUrl);
    factory.setTrustStore(trustStorePath);
    factory.setTrustStorePassword(trustStorePass);
    factory.setKeyStore(keyStorePath);
    factory.setKeyStorePassword(keyStorePass);
    factory.setUserName(userName);
    return factory;
  }

  /**
   * create JmsListenerContainerFactory.
   * 
   * @param connectionFactory ConnectionFactory
   * @param configurer        DefaultJmsListenerContainerFactoryConfigurer
   * @return
   */
  @Bean
  public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    return factory;
  }
}
