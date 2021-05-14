package it.francescofiora.tasks.taskexecutor.config;

import it.francescofiora.tasks.message.MessageDto;
import it.francescofiora.tasks.taskexecutor.config.parameter.JmsProperties;
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
   * Create a connectionFactory.
   *
   * @param properties JmsProperties
   * @return ConnectionFactory
   * @throws Exception if truststore/keystore path is wrong
   */
  @Profile("!dev")
  @Bean
  public ConnectionFactory connectionFactory(JmsProperties properties) throws Exception {
    ActiveMQSslConnectionFactory factory =
        new ActiveMQSslConnectionFactory(properties.getBrokerUrl());
    factory.setTrustStore(properties.getSsl().getTrustStorePath());
    factory.setTrustStorePassword(properties.getSsl().getKeyStorePass());
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
