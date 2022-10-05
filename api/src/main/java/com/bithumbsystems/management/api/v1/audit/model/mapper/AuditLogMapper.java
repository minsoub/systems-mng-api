package com.bithumbsystems.management.api.v1.audit.model.mapper;

import com.bithumbsystems.management.api.v1.audit.model.response.AuditLogDetailResponse;
import com.bithumbsystems.management.api.v1.audit.model.response.AuditLogResponse;
import com.bithumbsystems.management.api.v1.audit.model.response.AuditLogSearchResponse;
import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuditLogMapper {

  AuditLogMapper INSTANCE = Mappers.getMapper(AuditLogMapper.class);

  AuditLogSearchResponse auditLogToResponse(AuditLog auditLog);
  AuditLogResponse auditLogResponse(AuditLog auditLog);
  AuditLogDetailResponse auditLogDetailResponse(AuditLog auditLog);

}
