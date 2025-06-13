package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.SaveOrUpdateProgramWithRolesRequest;
import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.repositories.ProgramRepository;
import com.nisimsoft.auth_system.repositories.RoleRepository;
import com.nisimsoft.auth_system.utils.ProgramMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProgramService {
  private final ProgramRepository programRepository;
  private final RoleRepository roleRepository;

  public Program saveOrUpdateProgramWithRoles(SaveOrUpdateProgramWithRolesRequest request) {

    Long programId = request.getId();
    Long parentId = request.getParentId();

    Program program = programId != null
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
    List<Long> roleIds = request.getRoles();

    // Buscar los hijos por ID
    List<Program> foundChilds = programRepository.findAllById(childIds);
    // Buscar los roles por ID
    List<Role> foundRoles = roleRepository.findAllById(roleIds);

    if (foundChilds.size() != request.getChildren().size()) {
      throw new IllegalArgumentException("Uno o más programas hijos no existen en el sistema");
    }

    if (foundRoles.size() != request.getRoles().size()) {
      throw new IllegalArgumentException("Uno o más roles no existen en el sistema");
    }

    // Actualizar el set de permisos
    program.setChildren(new HashSet<>(foundChilds));
    program.setRoles(new HashSet<>(foundRoles));

    return programRepository.save(program);
  }

  public List<?> getProgramTree(Set<Role> userRoles, boolean includeRoles) {
    List<Program> rootPrograms = programRepository.findDistinctByRolesInAndParentIsNull(userRoles);

    if (includeRoles) {
      return rootPrograms.stream().map(ProgramMapper::toDTOWithRoles).toList();
    } else {
      return rootPrograms.stream().map(ProgramMapper::toDTOWithoutRoles).toList();
    }
  }
}
