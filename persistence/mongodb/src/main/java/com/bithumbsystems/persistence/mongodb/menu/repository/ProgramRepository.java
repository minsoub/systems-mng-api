package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import java.util.List;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProgramRepository extends ReactiveMongoRepository<Program, String>, ProgramCustomRepository {

  Mono<Void> deleteBySiteIdAndId(String siteId, String id);

  Flux<Program> findBySiteIdAndIsUse(String siteId, Boolean isUse);

  Mono<Program> findBySiteIdAndId(String siteId, String id);

  Flux<Program> findByIdIn(List<String> programIds);


}
