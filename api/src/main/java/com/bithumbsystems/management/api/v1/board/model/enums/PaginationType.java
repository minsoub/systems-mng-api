package com.bithumbsystems.management.api.v1.board.model.enums;

import com.bithumbsystems.management.api.core.model.enums.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaginationType implements EnumMapperType {

  BUTTON("버튼방식"),
  MORE("더보기방식"),
  SCROLL("스크롤방식");

  private final String title;

  @Override
  public String getCode() {
    return name();
  }

  public static String getTitle(String code) {
    for (PaginationType paginationType : PaginationType.values()) {
      if (code.equals(paginationType.getCode())) {
        return paginationType.getTitle();
      }
    }
    return null;
  }
}
