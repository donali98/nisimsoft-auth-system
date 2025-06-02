package com.nisimsoft.auth_system.utils;

import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseDTO;
import com.nisimsoft.auth_system.entities.Program;

import java.util.Comparator;
import java.util.List;

public class ProgramMapper {

  public static ProgramResponseDTO toDTO(Program program) {
    List<ProgramResponseDTO> childDTOs = program.getChildren().stream()
        .sorted(Comparator.comparing(Program::getName, String.CASE_INSENSITIVE_ORDER)) // orden alfabético
        .map(ProgramMapper::toDTO)
        .toList();

    return new ProgramResponseDTO(
        program.getId(),
        program.getName(),
        program.getUri(),
        program.getIcon(),
        program.getPinned(),
        childDTOs);
  }
}