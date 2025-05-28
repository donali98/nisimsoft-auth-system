package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Program;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {
  List<Program> findAllByParentIsNull();
}
