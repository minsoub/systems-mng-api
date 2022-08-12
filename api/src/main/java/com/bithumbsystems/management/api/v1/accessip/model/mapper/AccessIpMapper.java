package com.bithumbsystems.management.api.v1.accessip.model.mapper;

import com.bithumbsystems.management.api.v1.accessip.model.response.AccessIpDetailResponse;
import com.bithumbsystems.management.api.v1.accessip.model.response.AccessIpResponse;
import com.bithumbsystems.persistence.mongodb.accessip.model.entity.AccessIp;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccessIpMapper {

  AccessIpResponse accessIpToResponse(AccessIp accessIp);

  AccessIpDetailResponse accessIpToDetailResponse(AccessIp accessIp);

}