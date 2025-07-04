package com.nisimsoft.auth_system.dtos.responses.user;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesLoginDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import java.util.List;

@JsonPropertyOrder({ "id", "name", "username", "email", "corporations", "roles", "status" })
public record UserResponseDTO(
                Long id,
                String name,
                String username,
                String email,
                List<CorporationResponseDTO> corporations,
                List<RoleResponseDTO> roles,
                List<ProgramResponseWithoutRolesLoginDTO> menus,
                Boolean status) {
}
