package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    public Mono<UserAccount> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}

