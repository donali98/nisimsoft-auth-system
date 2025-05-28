package com.nisimsoft.auth_system.repositories;

import com.nisimsoft.auth_system.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {}
