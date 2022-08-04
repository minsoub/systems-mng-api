package com.bithumbsystems.management.api.v1.multilingual.model.mapper;

import com.bithumbsystems.management.api.v1.multilingual.model.response.MultilingualResponse;
import com.bithumbsystems.persistence.mongodb.multilingual.model.entity.SiteMultilingual;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MultilingualMapper {

  MultilingualResponse multilingualToResponse(SiteMultilingual siteMultilingual);
}
