package com.bithumbsystems.management.api.core.config;

import lombok.Getter;
import com.bithumbsystems.management.api.core.config.property.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@Getter
@Configuration
@Profile("dev|prod|eks-dev")
@RequiredArgsConstructor
public class AwsConfig {

    private final AwsProperties awsProperties;

    @Bean
    public S3AsyncClient s3client() {
        return S3AsyncClient.builder()
            .region(Region.of(awsProperties.getRegion()))
            .build();
    }
}