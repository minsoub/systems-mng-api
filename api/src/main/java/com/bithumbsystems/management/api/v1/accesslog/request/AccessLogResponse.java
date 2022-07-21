package com.bithumbsystems.management.api.v1.accesslog.request;

import com.bithumbsystems.persistence.mongodb.accesslog.model.enums.ActionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessLogResponse {
    @Schema(description = "id")
    private String id;
    @Schema(description = "관리자 계정 KEY")
    private String accountId;
    @Schema(description = "관리자 계정 ID")
    private String email;
    @Schema(description = "접속 IP")
    private String ip;
    @Schema(description = "수행업무 타입")
    private ActionType actionType;
    @Schema(description = "사유")
    private String reason;
    @Schema(description = "설명")
    private String description;
    @Schema(description = "사이트 ID")
    private String siteId;
    @Schema(description = "입력시간")
    private LocalDateTime createDate;
}
