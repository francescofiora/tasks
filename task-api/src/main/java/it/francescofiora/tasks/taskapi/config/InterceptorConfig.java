package it.francescofiora.tasks.taskapi.config;

import it.francescofiora.tasks.taskapi.web.api.interceptor.EndPointInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor Config.
 */
@Profile("Logging")
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

  @Autowired
  private EndPointInterceptor endPointInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(endPointInterceptor);
  }
}
