package com.bithumbsystems.management.api.core.util;

import static com.bithumbsystems.persistence.mongodb.common.model.constant.CommonConstant.DATE_TIME_PATTERN;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonUtil {

  public static final Gson gson = new GsonBuilder()
      .disableHtmlEscaping()
//      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .setDateFormat(DATE_TIME_PATTERN)
      .registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter())
      .setPrettyPrinting()
      .create();

  public static class GsonLocalDateTimeAdapter implements JsonSerializer<LocalDateTime>,
      JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type srcType,
        JsonSerializationContext context) {
      return new JsonPrimitive(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).format(localDateTime));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT,
        JsonDeserializationContext context) throws JsonParseException {
      return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }
  }
}
