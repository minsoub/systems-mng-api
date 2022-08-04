package com.bithumbsystems.management.api.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailProperties {
    private String logoUrl;
    private String loginUrl;
}
