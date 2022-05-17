package com.bithumbsystems.management.api.core.config;

import static com.bithumbsystems.management.api.core.config.constant.ParameterStoreConstant.*;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.bithumbsystems.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.management.api.core.config.property.MongoProperties;
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
    private AWSSimpleSystemsManagement awsSimpleSystemsManagement;

    private MongoProperties mongoProperties;

    private final AwsProperties awsProperties;

    private final CredentialsProvider credentialsProvider;

    @PostConstruct
    public void init() {

        log.debug("config store [prefix] => {}", awsProperties.getPrefix());
        log.debug("config store [name] => {}", awsProperties.getParamStoreName());
        log.debug("config store [profile] => {}", awsProperties.getProfileName());

        if (credentialsProvider.getActiveProfiles().equals("local")) {
            log.debug("keyId => {}", credentialsProvider.getProvider().resolveCredentials().accessKeyId());
            this.ssmClient = SsmClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                .credentialsProvider(credentialsProvider.getProvider())
                .build();
        }else {
            this.ssmClient = SsmClient.builder()
                .region(Region.of(awsProperties.getRegion()))
                //.credentialsProvider(credentialsProvider.awsCredentialsProvider())
                .build();
        }

        log.debug("keyId => {}", credentialsProvider.getProvider().resolveCredentials().accessKeyId());

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(
            credentialsProvider.getProvider().resolveCredentials().accessKeyId(),
            credentialsProvider.getProvider().resolveCredentials().secretAccessKey()
        );

        this.awsSimpleSystemsManagement = AWSSimpleSystemsManagementClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .withRegion(awsProperties.getRegion()).build();

        this.mongoProperties = new MongoProperties(
            getParameterValue(DB_URL),
            getParameterValue(USER),
            getParameterValue(PASSWORD),
            getParameterValue(PORT),
            getParameterValue(DB_NAME)
        );
    }

    protected String getParameterValue(String type) {
        String parameterName = String.format("%s/%s_%s/%s", awsProperties.getPrefix(), awsProperties.getParamStoreName(), awsProperties.getProfileName(), type);

        GetParameterRequest request = GetParameterRequest.builder()
            .name(parameterName)
            .withDecryption(true)
            .build();

        GetParameterResponse response = ssmClient.getParameter(request);

        return response.parameter().value();
    }
}
