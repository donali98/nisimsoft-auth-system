package com.nisimsoft.auth_system.dtos.requests;

import com.nisimsoft.auth_system.entities.enums.PermissionTypeEnum;
import lombok.Data;

@Data
public class PermissionFilterRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String search;
    private String sortColumn;
    private String sortOrder;
    private PermissionTypeEnum type;
}
