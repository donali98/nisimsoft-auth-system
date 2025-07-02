package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.SaveRoleRequest;
import com.nisimsoft.auth_system.entities.Permission;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.repositories.PermissionRepository;
import com.nisimsoft.auth_system.repositories.RoleRepository;
import com.nisimsoft.auth_system.specifications.RoleSpecification;

import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository roleRepository;
  private final PermissionRepository permissionRepository;

  @Transactional
  public Role saveOrUpdateRoleWithPermissions(SaveRoleRequest request) {

    Role role = roleRepository
        .findByName(request.getName())
        .orElseGet(Role::new); // Si no existe, crea uno nuevo

    role.setName(request.getName());
    role.setDescription(request.getDescription());

    List<Long> permissionIds = request.getPermissions();
    // Buscar los permisos por ID
    List<Permission> foundPermissions = permissionRepository.findAllById(permissionIds);

    if (foundPermissions.size() != request.getPermissions().size()) {
      throw new IllegalArgumentException("Uno o más permisos no existen en el sistema");
    }

    // Actualizar el set de permisos
    role.setPermissions(new HashSet<>(foundPermissions));

    return roleRepository.save(role);
  }

  public Page<Role> getRoles(
      int pageNumber,
      int pageSize,
      String searchWord,
      String sortColumn,
      String sortOrder,
      Map<String, Object> filters) {

    Sort sort = Sort.unsorted();

    if (sortColumn != null && sortOrder != null) {
      sort = Sort.by(Sort.Direction.fromString(sortOrder), sortColumn);
    }

    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

    Specification<Role> spec = RoleSpecification.build(searchWord, filters);

    return roleRepository.findAll(spec, pageable);
  }
}
