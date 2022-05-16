package com.bithumbsystems.management.api.core.config;

import static com.bithumbsystems.management.api.core.config.constant.ParameterStoreConstant.*;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.bithumbsystems.management.api.core.config.property.AwsProperty;
import com.bithumbsystems.management.api.core.config.property.MongoProperty;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Log4j2
@Data
@Profile("dev|prod")
@Configuration
public class ParameterStoreConfig {

    private AWSSimpleSystemsManagement awsSimpleSystemsManagement;

    private MongoProperty mongoProperty;

    private final AwsProperty awsProperty;

    private final CredentialsProvider credentialsProvider;

    @PostConstruct
    public void init() {

        log.debug("config store [prefix] => {}", awsProperty.getPrefix());
        log.debug("config store [name] => {}", awsProperty.getParamStoreName());
        log.debug("config store [profile] => {}", awsProperty.getProfileName());

        log.debug("keyId => {}", credentialsProvider.getProvider().resolveCredentials().accessKeyId());

        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(
            credentialsProvider.getProvider().resolveCredentials().accessKeyId(),
            credentialsProvider.getProvider().resolveCredentials().secretAccessKey()
        );

        this.awsSimpleSystemsManagement = AWSSimpleSystemsManagementClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(awsProperty.getRegion()).build();

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
        GetParameterRequest request = new GetParameterRequest();
        request.setName(parameterName);
        request.setWithDecryption(true);
        return awsSimpleSystemsManagement.getParameter(request).getParameter().getValue();
    }
}
