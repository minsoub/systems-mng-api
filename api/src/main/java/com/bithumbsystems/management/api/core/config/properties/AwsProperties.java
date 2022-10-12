package com.bithumbsystems.management.api.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class AwsProperties {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Value("${cloud.aws.param-store.prefix}")
  private String prefix;

  @Value("${cloud.aws.param-store.doc-name}")
  private String paramStoreDocName;
  @Value("${cloud.aws.param-store.kms-name}")
  private String paramStoreKmsName;
  @Value("${cloud.aws.param-store.salt-name}")
  private String paramStoreSaltName;
  @Value("${cloud.aws.param-store.iv-name}")
  private String paramStoreIvName;
  @Value("${cloud.aws.param-store.message-name}")
  private String paramStoreMessageName;
  @Value("${cloud.aws.param-store.auth-name}")
  private String paramStoreAuthName;
  @Value("${cloud.aws.param-store.crypto-name}")
  private String paramCryptoName;

  @Value("${cloud.aws.ssm.endpoint}")
  private String ssmEndPoint;
  @Value("${cloud.aws.ses.endpoint}")
  private String sesEndPoint;
  @Value("${cloud.aws.sqs.endpoint}")
  private String sqsEndPoint;
  @Value("${cloud.aws.sqs.audit.queue-name}")
  private String sqsAuditQueueName;
  @Value("${cloud.aws.sqs.authorization.queue-name}")
  private String sqsAuthorizationQueueName;
  @Value("${cloud.aws.kms.endpoint}")
  private String kmsEndPoint;

  private String kmsKey;
  private String saltKey;
  private String ivKey;
  // 접근 제어 IP 전송 SQS URL
  private String sqsAccessIpUrl;
  private String sqsAuthorizationUrl;
  private String jwtSecretKey;
  private String cryptoKey;
  @Value("${cloud.aws.ses.port}")
  private String smtpPort;
  @Setter
  private String emailSender;
  @Setter
  private String smtpUserName;
  @Setter
  private String smtpUserPassword;
}
