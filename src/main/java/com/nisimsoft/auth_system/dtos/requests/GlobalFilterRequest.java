package com.nisimsoft.auth_system.dtos.requests;

import lombok.Data;

@Data
public class GlobalFilterRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String search;
    private String sortColumn;
    private String sortOrder;
}
