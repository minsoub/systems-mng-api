package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import reactor.core.publisher.Flux;

public interface ProgramCustomRepository {

  Flux<Program> findBySearchText(String siteId, String searchText, Boolean isUse);
}
