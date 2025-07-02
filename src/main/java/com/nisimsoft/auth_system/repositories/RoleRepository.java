package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
  Optional<Role> findByName(String name);
}
