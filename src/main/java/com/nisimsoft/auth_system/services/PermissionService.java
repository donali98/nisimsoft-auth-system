package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.SavePermissionRequest;
import com.nisimsoft.auth_system.entities.Permission;
import com.nisimsoft.auth_system.repositories.PermissionRepository;
import com.nisimsoft.auth_system.specifications.PermissionSpecification;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class PermissionService {

  private final PermissionRepository permissionRepository;

  public Permission savePermission(SavePermissionRequest request) {
    Permission permission = new Permission();

    permission.setName(request.getName());
    permission.setValue(request.getValue());
    permission.setType(request.getType());

    return permissionRepository.save(permission);
  }

  public Page<Permission> getPermissions(
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

    Specification<Permission> spec = PermissionSpecification.build(searchWord, filters);

    return permissionRepository.findAll(spec, pageable);
  }
}
