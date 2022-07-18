package com.bithumbsystems.management.api.v1.board.model.enums;

import com.bithumbsystems.management.api.core.model.enums.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@RequiredArgsConstructor
public enum BoardType implements EnumMapperType {

  LIST("목록형"),
  CARD("카드형"),
  NOTICE("공지형");

  private final String title;

  @Override
  public String getCode() {
    return name();
  }

  public static String getTitle(String code) {
    if (StringUtils.hasLength(code)) {
      for (BoardType boardType : BoardType.values()) {
        if (code.equals(boardType.getCode())) {
          return boardType.getTitle();
        }
      }
    }
    return null;
  }
}
