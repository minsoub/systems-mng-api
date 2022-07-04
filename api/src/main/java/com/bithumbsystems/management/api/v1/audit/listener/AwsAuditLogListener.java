package com.bithumbsystems.management.api.v1.audit.listener;

import com.bithumbsystems.management.api.v1.audit.model.request.AuditLogRequest;
import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import com.bithumbsystems.persistence.mongodb.audit.model.enums.Crud;
import com.bithumbsystems.persistence.mongodb.audit.model.enums.Device;
import com.bithumbsystems.persistence.mongodb.audit.service.AuditLogDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.MenuDomainService;
import com.bithumbsystems.persistence.mongodb.menu.service.ProgramDomainService;
import com.bithumbsystems.persistence.mongodb.role.model.enums.RoleType;
import com.bithumbsystems.persistence.mongodb.site.service.SiteDomainService;
import com.google.gson.Gson;
import com.nimbusds.jose.shaded.json.JSONArray;
import io.awspring.cloud.messaging.listener.Acknowledgment;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsAuditLogListener {
  private final ReactiveJwtDecoder reactiveJwtDecoder;
  private final SiteDomainService siteDomainService;
  private final MenuDomainService menuDomainService;
  private final ProgramDomainService programDomainService;

  private final AuditLogDomainService auditLogDomainService;

  @SqsListener(value = {"${cloud.aws.sqs.queue-name}"}, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
  private void auditLogMessage(@Headers Map<String, String> header, @Payload String message,
      Acknowledgment ack) {
    log.debug("header: {} message: {}", header, message);
    AuditLogRequest auditLogRequest = new Gson().fromJson(message, AuditLogRequest.class);

    var auditLog = AuditLog.builder()
        .ip(auditLogRequest.getUserIp())
        .mySiteId(auditLogRequest.getMySiteId())
        .siteId(auditLogRequest.getSiteId())
        .method(auditLogRequest.getMethod())
        .crud(Crud.valueOf(auditLogRequest.getMethod()).getCrud())
        .uri(URLDecoder.decode(auditLogRequest.getUri(), StandardCharsets.UTF_8))
        .path(auditLogRequest.getPath())
        .parameter(auditLogRequest.getRequestBody())
        .queryParams(URLDecoder.decode(auditLogRequest.getQueryParams(), StandardCharsets.UTF_8))
        .createDate(LocalDateTime.now())
        .device(checkDevice(auditLogRequest.getUserAgent()).name())
        .referer(auditLogRequest.getReferer())
        .message(auditLogRequest.getToken() == null ? auditLogRequest.getMessage() : auditLogRequest.getToken())
        .build();

    siteDomainService.findById(auditLog.getSiteId())
        .flatMap(site -> {
          log.info("site {}", Thread.currentThread().getName());
          auditLog.setSiteName(site.getName());
          return userMappingJob(auditLogRequest, auditLog);
        })
        .flatMap(audit -> {
          log.info("audit {}", Thread.currentThread().getName());
          return urlMappingJob(audit)
              .flatMap(t -> {
                log.info("save {}", Thread.currentThread().getName());
                return auditLogDomainService.save(t);
              })
              .collectList();
        }).subscribe();
  }

  private Mono<AuditLog> userMappingJob(AuditLogRequest auditLogRequest, AuditLog auditLog) {
    log.info("userMappingJob1 {}", Thread.currentThread().getName());
    if (!StringUtils.hasLength(auditLogRequest.getToken())) { // 비로그인
      auditLog.setRoleType(RoleType.ANONYMOUS);
      return Mono.just(auditLog);
    } else {
      final String BEARER_TYPE = "Bearer";
      final var token = auditLogRequest.getToken().substring(BEARER_TYPE.length()).trim();
      return reactiveJwtDecoder.decode(token).flatMap(jwt -> {
        log.info("reactiveJwtDecoder.decode {}", Thread.currentThread().getName());
        final var email = jwt.getClaim("iss").toString();
        final var roles = (JSONArray) jwt.getClaim("ROLE");

        auditLog.setEmail(email);
        auditLog.setRoles(roles.stream().map(role -> {
            if(role == RoleType.USER) auditLog.setRoleType(RoleType.USER);
            else auditLog.setRoleType(RoleType.ADMIN);
            return role.toString();
        }).collect(Collectors.toSet()));
        return Mono.just(auditLog);
      });
    }
  }

  private Flux<AuditLog> urlMappingJob(AuditLog auditLog) {
    log.info("urlMappingJob {}", Thread.currentThread().getName());
    AntPathMatcher pathMatcher = new AntPathMatcher();
    return menuDomainService.findAllUrls()
        .filter(menu -> pathMatcher.match(menu.getUrl(), auditLog.getPath()))
        .flatMap(m -> {
          log.info("urlMappingJob menu {}", Thread.currentThread().getName());
          auditLog.setMenuId(m.getId());
          auditLog.setMenuName(m.getName());
          return Mono.just(auditLog);
        })
        .flatMap(auditLog1 -> {
          log.info("urlMappingJob program {}", Thread.currentThread().getName());
          return programDomainService.findAllUrls(auditLog1.getMethod())
              .filter(program -> pathMatcher.match(program.getActionUrl(), auditLog1.getPath()))
              .flatMap(p -> {
                auditLog1.setProgramId(p.getId());
                auditLog1.setProgramName(p.getName());
                return Mono.just(auditLog1);
              });
        }).defaultIfEmpty(auditLog);
  }

  private Device checkDevice(String userAgent) {
    boolean mobile1 = userAgent.matches(
        ".*(iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson).*");
    boolean mobile2 = userAgent.matches(".*(LG|SAMSUNG|Samsung).*");

    if (mobile1 || mobile2) {
      return Device.MOBILE;
    } else {
      return Device.PC;
    }
  }
}
