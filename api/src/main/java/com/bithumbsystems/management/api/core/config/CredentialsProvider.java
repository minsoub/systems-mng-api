package com.bithumbsystems.management.api.core.config;


import com.bithumbsystems.management.api.core.config.property.AwsProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;

@Slf4j
@Getter
@Configuration
@RequiredArgsConstructor
public class CredentialsProvider {

    @Value("${spring.profiles.active:local}")
    private String activeProfiles;

    private final AwsProperty awsProperty;

    @Profile("local")
    @Bean
    public ProfileCredentialsProvider getProvider() {
        log.debug("CredentialsProvider profile name => {}", awsProperty.getProfileName());
        ProfileCredentialsProvider credentialsProvider = ProfileCredentialsProvider.builder()
                .profileName(awsProperty.getProfileName()).build();

        log.debug("key id => {}", credentialsProvider.resolveCredentials().accessKeyId());
        return credentialsProvider;
    }

}
