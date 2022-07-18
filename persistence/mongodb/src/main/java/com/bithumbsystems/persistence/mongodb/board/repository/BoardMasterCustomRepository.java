package com.bithumbsystems.persistence.mongodb.board.repository;

import com.bithumbsystems.persistence.mongodb.board.model.entity.BoardMaster;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BoardMasterCustomRepository {
  Flux<BoardMaster> findBySearchCondition(String siteId, Boolean isUse);
}
