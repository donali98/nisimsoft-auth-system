package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.RoleFilterRequest;
import com.nisimsoft.auth_system.dtos.requests.SaveRoleRequest;
import com.nisimsoft.auth_system.dtos.responses.PaginatedResponse;
import com.nisimsoft.auth_system.dtos.responses.roles.PermissionResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RolePermissionsResponseDTO;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.RoleService;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleController {

  @Autowired
  private RoleService roleService;

  @PostMapping("/roles")
  public ResponseEntity<?> saveOrUpdateRoleWithPermissions(@RequestBody @Valid SaveRoleRequest request) {

    Role savedRole = roleService.saveOrUpdateRoleWithPermissions(request);

    RolePermissionsResponseDTO responseDTO = new RolePermissionsResponseDTO(
        savedRole.getId(),
        savedRole.getName(),
        savedRole.getDescription(),
        savedRole.getPermissions().stream()
            .map(
                permission -> new PermissionResponseDTO(
                    permission.getId(),
                    permission.getName(),
                    permission.getValue(),
                    permission.getType()))
            .toList());

    return new Response("Rol guardado exitosamente", responseDTO, HttpStatus.CREATED);
  }

  @GetMapping("/roles")
  public ResponseEntity<?> getRoles(@ModelAttribute RoleFilterRequest request) {
    Map<String, Object> filters = new HashMap<>();

    if (request.getName() != null) {
      filters.put("name", request.getName());
    }
    if (request.getDescription() != null) {
      filters.put("description", request.getDescription());
    }

    Page<Role> result = roleService.getRoles(
        request.getPage(),
        request.getSize(),
        request.getSearch(),
        request.getSortColumn(),
        request.getSortOrder(),
        filters);

    return new Response("Roles obtenidos exitosamente", PaginatedResponse.fromPage(result), HttpStatus.OK);

  }
}
