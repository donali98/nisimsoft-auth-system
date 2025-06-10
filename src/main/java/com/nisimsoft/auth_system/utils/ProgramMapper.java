package com.nisimsoft.auth_system.utils;

import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithRolesDTO;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import com.nisimsoft.auth_system.entities.Program;

import java.util.List;

public class ProgramMapper {

        public static ProgramResponseWithoutRolesDTO toDTOWithoutRoles(Program program) {
                List<ProgramResponseWithoutRolesDTO> children = program.getChildren().stream()
                                .map(ProgramMapper::toDTOWithoutRoles)
                                .toList();

                return new ProgramResponseWithoutRolesDTO(
                                program.getId(),
                                program.getName(),
                                program.getUri(),
                                program.getIcon(),
                                program.getPinned(),
                                children);
        }

        public static ProgramResponseWithRolesDTO toDTOWithRoles(Program program) {
                List<ProgramResponseWithRolesDTO> children = program.getChildren().stream()
                                .map(ProgramMapper::toDTOWithRoles)
                                .toList();

                List<RoleResponseDTO> roles = program.getRoles().stream()
                                .map(role -> new RoleResponseDTO(role.getId(), role.getName(), role.getDescription()))
                                .toList();

                return new ProgramResponseWithRolesDTO(
                                program.getId(),
                                program.getName(),
                                program.getUri(),
                                program.getIcon(),
                                program.getPinned(),
                                children,
                                roles);
        }
}
