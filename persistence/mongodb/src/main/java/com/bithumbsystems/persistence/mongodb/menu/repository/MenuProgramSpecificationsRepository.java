package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.MenuProgramSpecification;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MenuProgramSpecificationsRepository extends ReactiveMongoRepository<MenuProgramSpecification, String> {

}
