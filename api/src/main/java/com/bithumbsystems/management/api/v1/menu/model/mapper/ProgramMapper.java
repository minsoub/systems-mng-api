package com.bithumbsystems.management.api.v1.menu.model.mapper;

import com.bithumbsystems.management.api.v1.menu.model.request.ProgramRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.ProgramUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.model.response.ProgramResponse;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProgramMapper {

  ProgramMapper INSTANCE = Mappers.getMapper(ProgramMapper.class);

  ProgramResponse programToProgramResponse(Program program);

  Program programRegisterRequestToProgram(ProgramRegisterRequest programRegisterRequest);

  Program programUpdateRequestToProgram(ProgramUpdateRequest programUpdateRequest);

}
