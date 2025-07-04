package com.nisimsoft.auth_system.dtos.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CorporationFilterRequest extends GlobalFilterRequest {
    private String name;
    private String dbName;
    private String username;
    private String host;
    private String dbEngine;

}
