package com.bithumbsystems.management.api.v1.messenger.model.mapper;

import com.bithumbsystems.management.api.v1.mail.model.response.SiteMailResponse;
import com.bithumbsystems.management.api.v1.messenger.model.dto.Messenger;
import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail;
import com.bithumbsystems.persistence.mongodb.messenger.model.entity.DaouMessenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MessengerMapper {
  Messenger.MessengerResponse daouMessengerToResponse(DaouMessenger daouMessenger);
}
