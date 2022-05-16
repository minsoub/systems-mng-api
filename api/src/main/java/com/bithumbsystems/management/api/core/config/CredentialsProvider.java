package com.bithumbsystems.management.api.core.config;


import com.bithumbsystems.management.api.core.config.property.AwsProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CredentialsProvider {

    private final AwsProperty awsProperty;

    @Bean
    public ProfileCredentialsProvider getProvider() {
        log.debug("CredentialsProvider profile name => {}", awsProperty.getProfileName());
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.builder()
                .profileName(awsProperty.getProfileName()).build();

        log.debug("key id => {}", credentialsProvider.resolveCredentials().accessKeyId());
        return credentialsProvider;
    }

}
