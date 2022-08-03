package com.bithumbsystems.management.api.v1.multilingual.model.mapper;

import com.bithumbsystems.management.api.v1.multilingual.model.dto.Multilingual;
import com.bithumbsystems.persistence.mongodb.multilingual.model.entity.SiteMultilingual;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MultilingualMapper {

  Multilingual.MultilingualResponse multilingualToResponse(SiteMultilingual siteMultilingual);

}
