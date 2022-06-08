package com.bithumbsystems.persistence.mongodb.menu.repository;

import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProgramRepository extends ReactiveMongoRepository<Program, String> {

}
