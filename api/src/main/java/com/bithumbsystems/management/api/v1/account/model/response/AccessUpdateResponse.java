package com.bithumbsystems.management.api.v1.account.model.response;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessUpdateResponse {
    private String id;
    private String email;
    private LocalDateTime lastLoginDate;
    private String name;
    private Status status;
    private LocalDate validStartDate;
    private LocalDate validEndDate;
    private LocalDateTime createDate;
}
