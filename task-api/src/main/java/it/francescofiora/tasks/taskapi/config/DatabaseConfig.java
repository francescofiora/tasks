package it.francescofiora.tasks.taskapi.config;

import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Database Configuration.
 */
@Configuration
@Profile("!MongoSsl")
@EnableMongoRepositories("it.francescofiora.tasks.taskapi.repository")
@Import(value = MongoAutoConfiguration.class)
public class DatabaseConfig {

}
