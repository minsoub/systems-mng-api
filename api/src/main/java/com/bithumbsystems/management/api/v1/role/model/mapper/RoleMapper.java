package com.bithumbsystems.management.api.v1.role.model.mapper;

import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementRegisterRequest;
import com.bithumbsystems.management.api.v1.role.model.request.RoleManagementUpdateRequest;
import com.bithumbsystems.management.api.v1.role.model.response.RoleManagementResponse;
import com.bithumbsystems.persistence.mongodb.role.model.entity.RoleManagement;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {

  RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

  RoleManagement registerRequestToRoleManagement(RoleManagementRegisterRequest request);

  RoleManagement updateRequestToRoleManagement(RoleManagementUpdateRequest request);


  RoleManagementResponse roleManagementToResponse(RoleManagement roleManagement);
}
