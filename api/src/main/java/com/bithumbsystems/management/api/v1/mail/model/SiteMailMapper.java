package com.bithumbsystems.management.api.v1.mail.model;

import com.bithumbsystems.management.api.v1.mail.model.response.SiteMailResponse;
import com.bithumbsystems.persistence.mongodb.mail.model.entity.SiteMail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SiteMailMapper {
  SiteMailResponse siteMailToSiteMailResponse(SiteMail siteMail);
}
