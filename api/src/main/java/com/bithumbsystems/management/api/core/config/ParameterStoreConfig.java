package com.bithumbsystems.management.api.core.config;

import com.bithumbsystems.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.management.api.core.config.properties.MongoProperties;
import java.net.URI;
import javax.annotation.PostConstruct;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

import static com.bithumbsystems.management.api.core.config.constant.ParameterStoreConstant.*;

@Log4j2
@Data
@Configuration
@Profile("dev|qa|prod|eks-dev")
public class ParameterStoreConfig {

    private SsmClient ssmClient;
    private MongoProperties mongoProperties;

    private final AwsProperties awsProperties;

    @Value("${cloud.aws.credentials.profile-name}")
    private String profileName;

    @PostConstruct
    public void init() {

        log.debug("config store [prefix] => {}", awsProperties.getPrefix());
        log.debug("config store [name] => {}", awsProperties.getParamStoreDocName());

        this.ssmClient = SsmClient.builder()
                .endpointOverride(URI.create(awsProperties.getSsmEndPoint()))
                .region(Region.of(awsProperties.getRegion()))
            .build();

        this.mongoProperties = new MongoProperties(
            getParameterValue(awsProperties.getParamStoreDocName(), DB_URL),
            getParameterValue(awsProperties.getParamStoreDocName(), DB_USER),
            getParameterValue(awsProperties.getParamStoreDocName(), DB_PASSWORD),
            getParameterValue(awsProperties.getParamStoreDocName(), DB_PORT),
            getParameterValue(awsProperties.getParamStoreDocName(), DB_NAME)
        );

        // KMS Parameter Key
        this.awsProperties.setKmsKey(getParameterValue(awsProperties.getParamStoreKmsName(), KMS_ALIAS_NAME));
        this.awsProperties.setSaltKey(getParameterValue(awsProperties.getParamStoreSaltName(), KMS_ALIAS_NAME));
        this.awsProperties.setIvKey(getParameterValue(awsProperties.getParamStoreIvName(), KMS_ALIAS_NAME));
        this.awsProperties.setEmailSender(getParameterValue(awsProperties.getParamStoreMessageName(), MAIL_SENDER));
        this.awsProperties.setSqsAccessIpUrl(getParameterValue(awsProperties.getParamStoreMessageName(), SQS_ACCESS_IP_URL));
        this.awsProperties.setSqsAuthorizationUrl(getParameterValue(awsProperties.getParamStoreMessageName(), SQS_AUTHORIZATION_URL));
        this.awsProperties.setJwtSecretKey(getParameterValue(awsProperties.getParamStoreAuthName(), JWT_SECRET_KEY));
        this.awsProperties.setCryptoKey(getParameterValue(awsProperties.getParamCryptoName(), CRYPTO_KEY));
        this.awsProperties.setSmtpUserName(getParameterValue(awsProperties.getParamStoreMessageName(), SMTP_USERNAME).trim());
        this.awsProperties.setSmtpUserPassword(getParameterValue(awsProperties.getParamStoreMessageName(), SMTP_PASSWORD).trim());
    }

    protected String getParameterValue(String storeName, String type) {
        String parameterName = String.format("%s/%s_%s/%s", awsProperties.getPrefix(), storeName, profileName, type);

        GetParameterRequest request = GetParameterRequest.builder()
            .name(parameterName)
            .withDecryption(true)
            .build();

        GetParameterResponse response = this.ssmClient.getParameter(request);

        return response.parameter().value();
    }
}
