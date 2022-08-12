package com.bithumbsystems.persistence.mongodb.accessip.model.entity;

import com.bithumbsystems.persistence.mongodb.util.base.entity.Date;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "admin_access_ip")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class AccessIp extends Date {
    @MongoId
    private String id;
    private String adminAccountId;
    private String siteId;
    private LocalDateTime validStartDate;
    private LocalDateTime validEndDate;
    private String allowIp;
    private String roleId;
    @Builder.Default
    @Setter
    private Boolean isUse = true;

}

