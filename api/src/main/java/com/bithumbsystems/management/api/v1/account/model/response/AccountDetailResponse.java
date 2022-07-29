package com.bithumbsystems.management.api.v1.account.model.response;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailResponse {

    private String siteId;
    private String id;
    private String name;
    private String email;
    private LocalDateTime createDate;
    private Status status;
    private String roleId;
    private String roleName;
    private Boolean isUse;
    private LocalDate validStartDate;
    private LocalDate validEndDate;

    public AccountDetailResponse(String siteId, String id, String name, String email, LocalDateTime createDate,
                                 Status status, String roleId, String roleName, Boolean isUse) {
        this.siteId = siteId;
        this.id = id;
        this.name = name;
        this.email = email;
        this.createDate = createDate;
        this.status = status;
        this.roleId = roleId;
        this.roleName = roleName;
        this.isUse = isUse;
    }
}
