package com.bithumbsystems.management.api.v1.site.model.mapper;

import com.bithumbsystems.management.api.v1.site.model.request.SiteRegisterRequest;
import com.bithumbsystems.management.api.v1.site.model.response.SiteResponse;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteMapper {

  SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

  @Mapping(target = "siteId", source = "id")
  @Mapping(target = "siteName", source = "name")
  SiteResponse siteToSiteResponse(Site site);

  @Mapping(target = "name", source = "siteName")
  Site siteRegisterRequestToSite(SiteRegisterRequest siteRegisterRequest);
}
