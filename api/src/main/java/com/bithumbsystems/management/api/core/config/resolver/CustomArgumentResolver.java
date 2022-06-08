package com.bithumbsystems.management.api.core.config.resolver;

import com.bithumbsystems.management.api.core.exception.InvalidTokenException;
import com.bithumbsystems.management.api.core.model.enums.ErrorCode;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
public class CustomArgumentResolver implements HandlerMethodArgumentResolver {

  private final ReactiveJwtDecoder reactiveJwtDecoder;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(CurrentUser.class);
  }

  @Override
  public Mono<Object> resolveArgument(MethodParameter parameter, BindingContext bindingContext,
      ServerWebExchange exchange) {
    var token = "";
    final String AUTHORIZATION = "Authorization";
    final String BEARER_TYPE = "Bearer";

    for (String value : Objects.requireNonNull(
        exchange.getRequest().getHeaders().get(AUTHORIZATION))) {
      if (value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
        token = value.substring(BEARER_TYPE.length()).trim();
      }
    }
    log.debug(token);
    return reactiveJwtDecoder.decode(token)
        .flatMap((jwt) -> {
          if(StringUtils.isAnyEmpty(jwt.getSubject(), jwt.getClaimAsString("account_id"), jwt.getClaimAsString("ROLE"))) {
            return Mono.error(new InvalidTokenException(ErrorCode.INVALID_TOKEN));
          }
          var siteId = jwt.getSubject();
          var accountId = jwt.getClaimAsString("account_id");
          var role = jwt.getClaimAsString("ROLE");
          return Mono.just(new Account(siteId, accountId, role));
        });
  }
}
