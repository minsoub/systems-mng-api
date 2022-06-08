package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MenuRepository extends ReactiveMongoRepository<Menu, String> {

}
