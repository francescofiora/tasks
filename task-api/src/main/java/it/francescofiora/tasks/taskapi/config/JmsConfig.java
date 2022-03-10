package it.francescofiora.tasks.taskapi.config;

import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.taskapi.config.parameter.JmsProperties;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;
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

/**
 * Jms Config.
 */
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
    var converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName(MessageDto.class.getName());
    return converter;
  }

  /**
   * Create a connectionFactory.
   *
   * @param properties JmsProperties
   * @return ConnectionFactory
   * @throws Exception if truststore/keystore path is wrong
   */
  @Profile("AmqSsl")
  @Bean
  public ConnectionFactory connectionFactory(JmsProperties properties) throws Exception {
    var factory = new ActiveMQSslConnectionFactory(properties.getBrokerUrl());
    factory.setTrustStore(properties.getSsl().getTrustStorePath());
    factory.setTrustStorePassword(properties.getSsl().getTrustStorePass());
    factory.setKeyStore(properties.getSsl().getKeyStorePath());
    factory.setKeyStorePassword(properties.getSsl().getKeyStorePass());
    factory.setUserName(properties.getUser());
    return factory;
  }

  /**
   * Create JmsListenerContainerFactory.
   *
   * @param connectionFactory ConnectionFactory
   * @param configurer DefaultJmsListenerContainerFactoryConfigurer
   * @return Jms ListenerContainer Factory
   */
  @Bean
  public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
      DefaultJmsListenerContainerFactoryConfigurer configurer) {
    var factory = new DefaultJmsListenerContainerFactory();
    configurer.configure(factory, connectionFactory);
    return factory;
  }
}
