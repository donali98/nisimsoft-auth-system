package com.nisimsoft.auth_system.dtos.responses.program;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.nisimsoft.auth_system.entities.Program;

@JsonPropertyOrder({ "id", "name", "uri", "parent", "icon", "pinned" })
public record ProgramResponseDTO(
        Long id,
        String name,
        String uri,
        Program parent,
        String icon,
        Boolean pinned) {

}
