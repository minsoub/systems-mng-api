package com.bithumbsystems.management.api.core.util.sender;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.bithumbsystems.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.management.api.core.model.request.AccessAllowIpRequest;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.entity.AuthorizationResource;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSQSSenderImpl<T> implements AwsSQSSender<T> {

  private final AmazonSQSAsync amazonSQS;
  private final AwsProperties awsProperties;
  private final ProgramDomainService programDomainService;

  /**
   * Usage : private final AwsSQSSender<AccessAllowIpRequest> sqsSender;
   * <p>
   * // 전송 객체 AccessAllowIpRequest.builder
   * <p>
   * sqsSender.sendMessage(accessAlowIpRequest, UUID.randomUUID().toString());
   *
   * @param accessAllowIpRequest
   * @param groupId
   * @return
   */
  @Override
  public SendMessageResult sendMessage(AccessAllowIpRequest accessAllowIpRequest, String groupId) {
    log.debug("AwsSQSSender Thread {}", Thread.currentThread().getName());
    return amazonSQS.sendMessage(
        new SendMessageRequest(awsProperties.getSqsEndPoint() + "/" + awsProperties.getSqsAuditQueueName(),
            new Gson().toJson(accessAllowIpRequest))
                .withMessageGroupId(groupId)
            .withMessageDeduplicationId(UUID.randomUUID().toString())
    );
  }

  @Override
  public SendMessageResult sendMessage(List<AuthorizationResource> authorizationResources,
      String roleManagementId) {
    log.debug("AwsSQSSender Thread {}", Thread.currentThread().getName());

    return amazonSQS.sendMessage(
        new SendMessageRequest(awsProperties.getSqsEndPoint() + "/" + awsProperties.getSqsAuthorizationQueueName(),
            new Gson().toJson(changeProgramString(authorizationResources)))
                .withMessageGroupId(roleManagementId)
            .withMessageDeduplicationId(UUID.randomUUID().toString())
    );
  }

  private List<String> changeProgramString(List<AuthorizationResource> authorizationResources) {
    List<String> programs = new ArrayList<>();
    authorizationResources.forEach(authorizationResource ->
            programDomainService.findPrograms(authorizationResource.getProgramId())
                .flatMap(program -> {
                  programs.add(program.getActionMethod() + "|" + program.getActionUrl());
                  return Mono.empty();
                }).subscribe());
    log.info(programs.toString());
    return programs;
  }
}
