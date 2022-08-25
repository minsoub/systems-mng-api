package com.bithumbsystems.management.api.core.config.local;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.bithumbsystems.management.api.core.config.properties.AwsProperties;
import java.net.URI;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@Getter
@Setter
@Configuration
@RequiredArgsConstructor
@Profile("local")
public class LocalAwsConfig {

  @Value("${cloud.aws.credentials.profile-name}")
  private String profileName;

  private final AwsProperties awsProperties;

  private final CredentialsProvider credentialsProvider;
  private KmsAsyncClient kmsAsyncClient;
  private com.amazonaws.auth.profile.ProfileCredentialsProvider provider;

  @Bean
  public S3AsyncClient s3client() {
    return S3AsyncClient.builder()
        .region(Region.of(awsProperties.getRegion()))
        .credentialsProvider(ProfileCredentialsProvider.create(profileName))
        .build();
  }

  @Bean
  public AmazonSQSAsync amazonSQS() {
    var endpointConfig = new AwsClientBuilder.EndpointConfiguration(
        awsProperties.getSqsEndPoint(),
        awsProperties.getRegion()
    );
    return AmazonSQSAsyncClientBuilder.standard()
        .withCredentials(provider)
//        .withRegion(awsProperties.getRegion())
        .withEndpointConfiguration(endpointConfig)
        .build();
  }

  @PostConstruct
  public void init() {
    kmsAsyncClient = KmsAsyncClient.builder()
        .region(Region.of(awsProperties.getRegion()))
        .endpointOverride(URI.create(awsProperties.getKmsEndPoint()))
        .credentialsProvider(ProfileCredentialsProvider.create(profileName))
        .build();

    provider = new com.amazonaws.auth.profile.ProfileCredentialsProvider(profileName);
  }
}