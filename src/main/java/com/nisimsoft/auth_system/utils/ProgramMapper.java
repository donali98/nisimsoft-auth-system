package com.nisimsoft.auth_system.utils;

import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithRolesDTO;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesDTO;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesLoginDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import com.nisimsoft.auth_system.entities.Permission;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.entities.enums.PermissionTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        public static ProgramResponseWithoutRolesLoginDTO toDTOWithSystemPermissions(
                        Program program,
                        Set<Permission> userPermissions) {
                // Intentamos extraer una clave de URI que permita asociar con los permisos
                String originalUri = program.getUri();
                final String uriKey;
                if (originalUri != null && originalUri.contains("-")) {
                        uriKey = originalUri.substring(originalUri.lastIndexOf('-') + 1); // Ej: "gestion-usuarios" →
                                                                                          // "usuarios"
                } else if (originalUri != null) {
                        uriKey = originalUri; // Si no contiene "-", usamos el URI completo
                } else {
                        uriKey = null;
                }

                List<String> systemPermissions = new ArrayList<>();

                if (uriKey != null) {
                        systemPermissions = userPermissions.stream()
                                        .filter(p -> p.getType() == PermissionTypeEnum.SYSTEM)
                                        .filter(p -> p.getValue().endsWith(uriKey)) // Ahora comparamos contra el sufijo
                                        .map(Permission::getValue)
                                        .toList();
                }

                List<ProgramResponseWithoutRolesLoginDTO> children = program.getChildren().stream()
                                .map(child -> toDTOWithSystemPermissions(child, userPermissions))
                                .toList();

                return new ProgramResponseWithoutRolesLoginDTO(
                                program.getId(),
                                program.getName(),
                                program.getUri(),
                                program.getIcon(),
                                Boolean.TRUE.equals(program.getPinned()),
                                children,
                                systemPermissions);
        }

}
