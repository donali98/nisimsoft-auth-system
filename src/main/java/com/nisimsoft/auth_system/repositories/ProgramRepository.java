package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Program;
import com.nisimsoft.auth_system.entities.Role;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
  List<Program> findAllByParentIsNull();

  /*
   * Consulta que ejecuta:
   * 
   * SELECT DISTINCT p FROM Program p
   * JOIN p.roles r
   * WHERE r IN :roles AND p.parent IS NULL
   *
   */
  List<Program> findDistinctByRolesInAndParentIsNull(Set<Role> roles);
}
