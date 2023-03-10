package com.bithumbsystems.management.api.v1.messenger.service;

import com.bithumbsystems.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.management.api.core.config.resolver.Account;
import com.bithumbsystems.management.api.core.util.AES256Util;
import com.bithumbsystems.management.api.v1.messenger.model.mapper.MessengerMapper;
import com.bithumbsystems.management.api.v1.messenger.model.request.MessengerRequest;
import com.bithumbsystems.management.api.v1.messenger.model.response.MessengerResponse;
import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger;
import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger.DaouMessengerBuilder;
import com.bithumbsystems.persistence.mongodb.messenger.service.DaouMessengerDomainService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessengerService {

  private final DaouMessengerDomainService daouMessengerDomainService;

  private final MessengerMapper messengerMapper;

  private final AwsProperties awsProperties;

  public Mono<List<MessengerResponse>> getMessengerList() {
    return daouMessengerDomainService.findAll().map(messengerMapper::daouMessengerToResponse).collectList();
  }

  public Mono<MessengerResponse> getMessenger(String id) {
    return daouMessengerDomainService.findById(id)
        .map(
            daouMessenger -> {
              MessengerResponse messengerResponse = messengerMapper.daouMessengerToResponse(daouMessenger);
              messengerResponse.decryptUserInfo(awsProperties.getKmsKey());
              return messengerResponse;
            }
        );
  }

  public Mono<MessengerResponse> createMessenger(MessengerRequest request, Account account) {
    DaouMessenger daouMessenger = makeDaouMessengerBuilder(request).id(UUID.randomUUID().toString()).build();

    daouMessenger.setCreateDate(LocalDateTime.now());
    daouMessenger.setCreateAdminAccountId(account.getAccountId());

    return daouMessengerDomainService.save(daouMessenger).map(messengerMapper::daouMessengerToResponse);
  }

  public Mono<MessengerResponse> updateMessenger(String id, MessengerRequest request, Account account) {
    DaouMessenger daouMessenger = makeDaouMessengerBuilder(request).id(id).build();

    return daouMessengerDomainService.findById(id)
        .flatMap(messenger -> {
          daouMessenger.setCreateDate(messenger.getCreateDate());
          daouMessenger.setCreateAdminAccountId(messenger.getCreateAdminAccountId());
          daouMessenger.setUpdateDate(LocalDateTime.now());
          daouMessenger.setUpdateAdminAccountId(account.getAccountId());

          return daouMessengerDomainService.update(daouMessenger).map(messengerMapper::daouMessengerToResponse);
        });
  }

  private DaouMessengerBuilder makeDaouMessengerBuilder(MessengerRequest request) {
    return DaouMessenger.builder()
        .host(request.getHost())
        .locale(request.getLocale())
        .loginApi(request.getLoginApi())
        .pubsubApi(request.getPubsubApi())
        .sessionApi(request.getSessionApi())
        .isUse(request.getIsUse())
        .user(
            AES256Util.encryptAES(awsProperties.getKmsKey(), request.getUser(), awsProperties.getSaltKey(), awsProperties.getIvKey())
        )
        .pass(
            AES256Util.encryptAES(awsProperties.getKmsKey(), request.getPass(), awsProperties.getSaltKey(), awsProperties.getIvKey())
        );
  }

}
