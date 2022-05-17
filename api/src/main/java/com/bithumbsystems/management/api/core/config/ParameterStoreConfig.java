package com.bithumbsystems.management.api.core.config;

import static com.bithumbsystems.management.api.core.config.constant.ParameterStoreConstant.*;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.bithumbsystems.management.api.core.config.property.AwsProperty;
import com.bithumbsystems.management.api.core.config.property.MongoProperty;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Log4j2
@Data
@Profile("dev|prod")
@Configuration
public class ParameterStoreConfig {

    private SsmClient ssmClient;
    private MongoProperty mongoProperty;

    private final AwsProperty awsProperty;

    private final CredentialsProvider credentialsProvider;

    @PostConstruct
    public void init() {

        log.debug("config store [prefix] => {}", awsProperty.getPrefix());
        log.debug("config store [name] => {}", awsProperty.getParamStoreName());
        log.debug("config store [profile] => {}", awsProperty.getProfileName());

        if (credentialsProvider.getActiveProfiles().equals("local")) {
            log.debug("keyId => {}", credentialsProvider.getProvider().resolveCredentials().accessKeyId());
            this.ssmClient = SsmClient.builder()
                    .region(Region.of(awsProperty.getRegion()))
                    .credentialsProvider(credentialsProvider.getProvider())
                    .build();
        }else {
            this.ssmClient = SsmClient.builder()
                    .region(Region.of(awsProperty.getRegion()))
                    //.credentialsProvider(credentialsProvider.awsCredentialsProvider())
                    .build();
        }

        this.mongoProperty = new MongoProperty(
                getParameterValue(DB_URL),
                getParameterValue(USER),
                getParameterValue(PASSWORD),
                getParameterValue(PORT),
                getParameterValue(DB_NAME)
        );
    }

    protected String getParameterValue(String type) {
        String parameterName = String.format("%s/%s_%s/%s", awsProperty.getPrefix(), awsProperty.getParamStoreName(), awsProperty.getProfileName(), type);

        software.amazon.awssdk.services.ssm.model.GetParameterRequest request = GetParameterRequest.builder()
                .name(parameterName)
                .withDecryption(true)
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);

        return response.parameter().value();
    }
}
