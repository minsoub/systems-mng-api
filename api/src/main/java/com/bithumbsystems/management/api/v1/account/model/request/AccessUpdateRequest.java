package com.bithumbsystems.management.api.v1.account.model.request;

import com.bithumbsystems.persistence.mongodb.account.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessUpdateRequest {
    private String id;
    private String name;
    private String email;
    private LocalDate validStartDate;
    private LocalDate validEndDate;
    private Status status;
    private Boolean isUse;
}
