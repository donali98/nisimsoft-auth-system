package com.nisimsoft.auth_system.dtos.responses.corporation;

import com.nisimsoft.auth_system.entities.enums.NSCorpDBEngineEnum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CorporationResponseDTO {
    private Long id;
    private String name;
    private String dbName;
    private String username;
    private String logo;
    private String host;
    private NSCorpDBEngineEnum dbEngine;
}
