package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.SaveProgramRequest;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseDTO;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.repositories.ProgramRepository;
import com.nisimsoft.auth_system.utils.ProgramMapper;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService {
  private final ProgramRepository programRepository;

  public Program saveOrUpdateProgram(SaveProgramRequest request) {

    Long programId = request.getId();
    Long parentId = request.getParentId();

    Program program =
        programId != null
            ? programRepository.findById(programId).orElseGet(Program::new)
            : new Program();

    if (parentId != null) {
      Program parent = programRepository.findById(parentId).orElse(null);
      program.setParent(parent);
    }

    program.setName(request.getName());
    program.setUri(request.getUri());
    program.setIcon(request.getIcon());
    program.setPinned(request.getPinned());

    List<Long> childIds = request.getChildren();

    // Buscar los hijos por ID
    List<Program> foundChilds = programRepository.findAllById(childIds);

    if (foundChilds.size() != request.getChildren().size()) {
      throw new IllegalArgumentException("Uno o más programas hijos no existen en el sistema");
    }

    // Actualizar el set de permisos
    program.setChildren(new HashSet<>(foundChilds));

    return programRepository.save(program);
  }

  public List<ProgramResponseDTO> getProgramTree() {
    List<Program> rootPrograms = programRepository.findAllByParentIsNull();

    return rootPrograms.stream().map(ProgramMapper::toDTO).toList();
  }
}
