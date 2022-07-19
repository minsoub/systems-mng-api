package com.bithumbsystems.management.api.v1.board.model.enums;

import com.bithumbsystems.management.api.core.model.enums.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
public enum AuthType implements EnumMapperType {

  ALL_USER("모든 사용자"),
  OWNER("소유자");

  private final String title;

  @Override
  public String getCode() {
    return name();
  }

  public static String getTitle(String code) {
    if (StringUtils.hasLength(code)) {
      for (AuthType authType : AuthType.values()) {
        if (code.equals(authType.getCode())) {
          return authType.getTitle();
        }
      }
    }
    return null;
  }
}
