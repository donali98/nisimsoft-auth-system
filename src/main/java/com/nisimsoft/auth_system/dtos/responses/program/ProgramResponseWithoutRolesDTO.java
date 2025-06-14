package com.nisimsoft.auth_system.dtos.responses.program;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "uri", "icon", "pinned", "children" })
public record ProgramResponseWithoutRolesDTO(Long id,
        String name,
        String uri,
        String icon,
        Boolean pinned,
        List<ProgramResponseWithoutRolesDTO> children) {

}
