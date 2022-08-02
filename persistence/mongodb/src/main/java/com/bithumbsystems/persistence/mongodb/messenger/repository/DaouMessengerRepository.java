package com.bithumbsystems.persistence.mongodb.messenger.repository;

import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DaouMessengerRepository extends ReactiveMongoRepository<DaouMessenger, String> {

}
