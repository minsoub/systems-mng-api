package com.bithumbsystems.management.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication(scanBasePackages = "com.bithumbsystems")
@EnableWebFlux
@ConfigurationPropertiesScan("com.bithumbsystems.management.api.core.config")
@EnableReactiveMongoRepositories("com.bithumbsystems.persistence.mongodb")
@EnableReactiveMongoAuditing
@OpenAPIDefinition(info = @Info(title = "System Management API", version = "1.0", description = "System Management APIs v1.0"))
public class ManagementApplication {

  public static void main(String[] args) {
    SpringApplication.run(ManagementApplication.class, args);
  }

  @Profile("dev|prod")
  @EnableAutoConfiguration(
      exclude = {
          MongoAutoConfiguration.class,
          MongoReactiveAutoConfiguration.class,
          MongoDataAutoConfiguration.class,
          EmbeddedMongoAutoConfiguration.class
      })
  static class WithoutAutoConfigurationMongo{}
}