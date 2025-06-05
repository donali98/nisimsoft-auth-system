package com.nisimsoft.auth_system.utils;

import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import com.nisimsoft.auth_system.entities.Program;
import java.util.List;

public class ProgramMapper {

  public static ProgramResponseDTO toDTO(Program program) {
    List<ProgramResponseDTO> childDTOs = program.getChildren().stream()
        .map(ProgramMapper::toDTO)
        .toList();

    List<RoleResponseDTO> roleDTOs = program.getRoles().stream()
        .map(role -> new RoleResponseDTO(
            role.getId(),
            role.getName(),
            role.getDescription()))
        .toList();

    return new ProgramResponseDTO(
        program.getId(),
        program.getName(),
        program.getUri(),
        program.getIcon(),
        program.getPinned(),
        childDTOs,
        roleDTOs);
  }
}
