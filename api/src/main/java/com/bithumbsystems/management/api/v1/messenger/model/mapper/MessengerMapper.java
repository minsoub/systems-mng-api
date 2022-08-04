package com.bithumbsystems.management.api.v1.messenger.model.mapper;

import com.bithumbsystems.management.api.v1.messenger.model.response.MessengerResponse;
import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessengerMapper {

  MessengerResponse daouMessengerToResponse(DaouMessenger daouMessenger);
}
