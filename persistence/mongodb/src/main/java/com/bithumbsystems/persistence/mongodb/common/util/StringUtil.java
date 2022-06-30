package com.bithumbsystems.persistence.mongodb.common.util;

import java.util.UUID;

public class StringUtil {

  public static String generateUUIDWithOutDash() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
