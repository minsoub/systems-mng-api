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
        .doOnNext(site -> auditLog.setSiteName(site.getName()))
//        .publishOn(Schedulers.boundedElastic())
            .mergeWith( t -> urlMappingJob(auditLog)
                .doOnSubscribe(v -> userMappingJob(auditLogRequest, auditLog))
                .doOnComplete(() -> {
                  log.info(auditLog.toString());
                  auditLogDomainService.save(auditLog)
                      .doFinally(v -> ack.acknowledge())
                      .subscribe();
                }).subscribe()
            ).subscribe();
  }

  private void userMappingJob(AuditLogRequest auditLogRequest, AuditLog auditLog) {
    if (!StringUtils.hasLength(auditLogRequest.getToken())) { // 비로그인
      auditLog.setRoleType(RoleType.ANONYMOUS);
    } else {
      final String BEARER_TYPE = "Bearer";
      final var token = auditLogRequest.getToken().substring(BEARER_TYPE.length()).trim();
      reactiveJwtDecoder.decode(token).flatMap(jwt -> {
        final var email = jwt.getClaim("iss").toString();
        final var roles = (JSONArray) jwt.getClaim("ROLE");

        auditLog.setEmail(email);
        auditLog.setRoles(roles.stream().map(role -> {
            if(role == RoleType.USER) auditLog.setRoleType(RoleType.USER);
            else auditLog.setRoleType(RoleType.ADMIN);
            return role.toString();
        }).collect(Collectors.toSet()));
        return Mono.just(jwt);
      }).subscribe();
    }
  }

  private Flux<Object> urlMappingJob(AuditLog auditLog) {
    AntPathMatcher pathMatcher = new AntPathMatcher();
    var findMenu = menuDomainService.findAllUrls()
        .filter(menu -> pathMatcher.match(menu.getUrl(), auditLog.getPath()))
        .flatMap(m -> {
          auditLog.setMenuId(m.getId());
          auditLog.setMenuName(m.getName());
          return Mono.empty();
        });

    var findProgram = programDomainService.findAllUrls(auditLog.getMethod())
        .filter(program -> pathMatcher.match(program.getActionUrl(), auditLog.getPath()))
        .flatMap(p -> {
          auditLog.setProgramId(p.getId());
          auditLog.setProgramName(p.getName());
          return Mono.empty();
        });
    return findMenu.mergeWith(findProgram);
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
