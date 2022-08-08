package com.bithumbsystems.management.api.core.model.request;

import com.bithumbsystems.management.api.core.model.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AccessAllowIpRequest {
    private JobType jobType;
    private String adminAccessId;
    private String siteId;
    private String roleId;
    private LocalDate validStartDate;
    private LocalDate validEndDate;
    private Set<String> allowIpList;
}
