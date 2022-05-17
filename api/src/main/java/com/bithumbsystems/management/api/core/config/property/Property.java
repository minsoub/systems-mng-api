package com.bithumbsystems.management.api.core.config.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
@Getter
public class Property {
    @Value("${cloud.aws.credentials.profile-name:dev}")
    private String profileName;
}