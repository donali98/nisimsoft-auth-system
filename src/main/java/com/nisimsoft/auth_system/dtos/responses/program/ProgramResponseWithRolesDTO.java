package com.nisimsoft.auth_system.dtos.responses.program;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;

import java.util.List;

@JsonPropertyOrder({ "id", "name", "uri", "icon", "pinned", "children", "roles" })
public record ProgramResponseWithRolesDTO(
                Long id,
                String name,
                String uri,
                String icon,
                Boolean pinned,
                List<ProgramResponseWithRolesDTO> children,
                List<RoleResponseDTO> roles) {
}
