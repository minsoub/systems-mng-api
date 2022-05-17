package com.bithumbsystems.management.api.core.config;


import lombok.Getter;
import com.bithumbsystems.management.api.core.config.property.AwsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class AwsConfig {

    private final AwsProperties awsProperties;

    private final CredentialsProvider credentialsProvider;

    @Bean
    public S3AsyncClient s3client() throws Exception {

        if (credentialsProvider.getActiveProfiles().equals("local")) {
            return S3AsyncClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(ProfileCredentialsProvider.create(awsProperties.getProfileName()))
                .build();
        }else if(credentialsProvider.getActiveProfiles().equals("dev")) {
            log.debug("activeProfile => {}", credentialsProvider.getActiveProfiles());
            return S3AsyncClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                //.credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();
        }else if(credentialsProvider.getActiveProfiles().equals("prop")) {
            return S3AsyncClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                //.credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();
        }else {
            throw new Exception("Not profile aws credentails...");
        }
    }
}