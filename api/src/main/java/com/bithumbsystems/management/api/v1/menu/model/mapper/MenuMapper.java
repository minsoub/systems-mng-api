package com.bithumbsystems.management.api.v1.menu.model.mapper;

import com.bithumbsystems.management.api.v1.menu.model.request.MenuRegisterRequest;
import com.bithumbsystems.management.api.v1.menu.model.request.MenuUpdateRequest;
import com.bithumbsystems.management.api.v1.menu.model.response.MenuResponse;
import com.bithumbsystems.persistence.mongodb.menu.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MenuMapper {

  MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

  MenuResponse menuToMenuResponse(Menu menu);

  Menu menuRegisterRequestToMenu(MenuRegisterRequest menuRegisterRequest);

  Menu menuUpdateRequestToMenu(MenuUpdateRequest menuUpdateRequest);

}
