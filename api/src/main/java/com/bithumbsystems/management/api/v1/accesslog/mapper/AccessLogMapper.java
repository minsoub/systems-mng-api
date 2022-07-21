package com.bithumbsystems.management.api.v1.accesslog.mapper;

import com.bithumbsystems.management.api.v1.accesslog.request.AccessLogResponse;
import com.bithumbsystems.persistence.mongodb.accesslog.model.entity.AccessLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccessLogMapper {
    AccessLogMapper INSTANCE = Mappers.getMapper(AccessLogMapper.class);

    AccessLogResponse accessLogResponse(AccessLog accessLog);

}
