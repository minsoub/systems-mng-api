package com.bithumbsystems.management.api.v1.site.model.mapper;

import com.bithumbsystems.management.api.v1.site.model.request.SiteFileInfoRequest;
import com.bithumbsystems.management.api.v1.site.model.request.SiteRegisterRequest;
import com.bithumbsystems.management.api.v1.site.model.response.SiteFileInfoResponse;
import com.bithumbsystems.management.api.v1.site.model.response.SiteResponse;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import com.bithumbsystems.persistence.mongodb.site.model.entity.SiteFileInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SiteMapper {

  SiteMapper INSTANCE = Mappers.getMapper(SiteMapper.class);

  SiteResponse siteToSiteResponse(Site site);

  Site siteRegisterRequestToSite(SiteRegisterRequest siteRegisterRequest);

  SiteFileInfoResponse siteFileInfoToResponse(SiteFileInfo fileInfo);

  SiteFileInfo siteFileInfoRequestToSiteFileInfo(SiteFileInfoRequest siteFileInfoRequest);
}
