package com.bithumbsystems.persistence.mongodb.messenger.service;

import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger;
import com.bithumbsystems.persistence.mongodb.messenger.repository.DaouMessengerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class DaouMessengerDomainService {

  private final DaouMessengerRepository daouMessengerRepository;

  public Flux<DaouMessenger> findAll() {
    return daouMessengerRepository.findAll();
  }

  public Mono<DaouMessenger> findById(String id) {
    return daouMessengerRepository.findById(id);
  }

  public Mono<DaouMessenger> save(DaouMessenger daouMessenger) {
    return daouMessengerRepository.insert(daouMessenger);
  }

  public Mono<DaouMessenger> update(DaouMessenger daouMessenger) {
    return daouMessengerRepository.save(daouMessenger);
  }

}
