package com.bithumbsystems.management.api.core.config.resolver;

import static com.bithumbsystems.management.api.core.util.GsonUtil.gson;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class QueryParamArgumentResolver implements HandlerMethodArgumentResolver {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(QueryParam.class);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
      ServerWebExchange exchange) {
    var json = gson.toJson(exchange.getRequest().getQueryParams().toSingleValueMap());
    return Mono.just(gson.fromJson(json, parameter.getParameterType()));
  }

}