package com.nisimsoft.auth_system.controllers;

import com.nisimsoft.auth_system.dtos.requests.LoginRequest;
import com.nisimsoft.auth_system.dtos.requests.SaveOrUpdateUserRequest;
import com.nisimsoft.auth_system.dtos.requests.ToggleUserStatusRequest;
import com.nisimsoft.auth_system.dtos.requests.UserFilterRequest;
import com.nisimsoft.auth_system.dtos.requests.VerifyUserRequest;
import com.nisimsoft.auth_system.dtos.responses.PaginatedResponse;
import com.nisimsoft.auth_system.dtos.responses.program.ProgramResponseWithoutRolesLoginDTO;
import com.nisimsoft.auth_system.dtos.responses.roles.RoleResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.CorporationResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.EnableDisableUserResponseDTO;
import com.nisimsoft.auth_system.dtos.responses.user.UserResponseDTO;
import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.entities.Permission;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.entities.User;
import com.nisimsoft.auth_system.exceptions.auth.AuthenticationFailedException;
import com.nisimsoft.auth_system.responses.Response;
import com.nisimsoft.auth_system.services.AuthProviderFactory;
import com.nisimsoft.auth_system.services.AuthenticationService;
import com.nisimsoft.auth_system.services.ProgramService;
import com.nisimsoft.auth_system.services.providers.AuthenticationProvider;
import com.nisimsoft.auth_system.utils.JwtUtils;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {
  @Autowired
  private AuthenticationService authenticationService;

  @Autowired
  private ProgramService programService;

  @Autowired
  private AuthProviderFactory authProviderFactory;

  @Autowired
  private JwtUtils jwtUtils;

  @Value("${app.auth.provider}")
  private String activeAuthProvider;

  @PostMapping("/users")
  public ResponseEntity<?> saveOrUpdateUser(@Valid @RequestBody SaveOrUpdateUserRequest request) {

    // Registrar usuario
    User user = authenticationService.saveOrUpdateUser(request);

    Set<Role> roles = user.getRoles();

    Set<Permission> permissions = roles.stream()
        .flatMap(role -> role.getPermissions().stream())
        .collect(Collectors.toSet());

    List<ProgramResponseWithoutRolesLoginDTO> programTree = programService.getProgramTreeWithActions(roles,
        permissions);

    UserResponseDTO responseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        mapCorporations(user.getCorporations()),
        mapRoles(user.getRoles()),
        programTree,
        user.isActive());

    String responseMessage = request.getId() != null
        ? "Usuario actualizado exitosamente"
        : "Usuario registrado exitosamente";

    return new Response(responseMessage, responseDTO, HttpStatus.CREATED);
  }

  @PostMapping("/verify-user")
  public ResponseEntity<?> verifyUser(@Valid @RequestBody VerifyUserRequest request) {
    // AuthenticationProvider provider = getAuthenticationProvider();

    User user = authenticationService.getUserByEmailOrThrow(request.getEmail());

    // authenticationService.authenticateOrThrow(provider, request.getEmail(),
    // request.getPassword());

    Set<Corporation> safeCorporations = new HashSet<>(user.getCorporations());

    List<CorporationResponseDTO> corporationDTOs = mapCorporations(safeCorporations);

    Set<Role> roles = user.getRoles();

    Set<Permission> permissions = roles.stream()
        .flatMap(role -> role.getPermissions().stream())
        .collect(Collectors.toSet());

    List<ProgramResponseWithoutRolesLoginDTO> programTree = programService.getProgramTreeWithActions(roles,
        permissions);

    UserResponseDTO responseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        corporationDTOs,
        mapRoles(user.getRoles()),
        programTree,
        user.isActive());

    return new Response("Usuario encontrado exitosamente", responseDTO, HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    AuthenticationProvider provider = getAuthenticationProvider();

    User user = authenticationService.getUserByIdOrThrow(request.getUserId());

    authenticationService.validateUserBelongsToCorporation(user, request.getCorpId());

    authenticationService.authenticateOrThrow(provider, user.getEmail(), request.getPassword());

    Set<Role> roles = user.getRoles();
    Set<Permission> permissions = roles.stream()
        .flatMap(role -> role.getPermissions().stream())
        .collect(Collectors.toSet());

    String token = jwtUtils.generateToken(user.getEmail(), request.getCorpId().toString());

    List<ProgramResponseWithoutRolesLoginDTO> programTree = programService.getProgramTreeWithActions(roles,
        permissions);

    UserResponseDTO userResponseDTO = new UserResponseDTO(
        user.getId(),
        user.getName(),
        user.getUsername(),
        user.getEmail(),
        mapCorporations(user.getCorporations()),
        mapRoles(user.getRoles()),
        programTree,
        user.isActive());

    return new Response(
        "Autenticación exitosa", Map.of("user", userResponseDTO, "token", token), HttpStatus.OK);
  }

  @GetMapping("/users")
  public ResponseEntity<?> getUsers(@ModelAttribute UserFilterRequest request) {
    Map<String, Object> filters = new HashMap<>();

    if (request.getName() != null) {
      filters.put("name", request.getName());
    }
    if (request.getUsername() != null) {
      filters.put("username", request.getUsername());
    }
    if (request.getEmail() != null) {
      filters.put("email", request.getEmail());
    }

    filters.put("isActive", request.isActive());

    Page<User> result = authenticationService.getUsers(
        request.getPage(),
        request.getSize(),
        request.getSearch(),
        request.getSortColumn(),
        request.getSortOrder(),
        filters);

    return new Response("Usuarios obtenidos exitosamente", PaginatedResponse.fromPage(result), HttpStatus.OK);

  }

  @DeleteMapping("/users")
  public ResponseEntity<?> toggleUserStatus(@Valid @RequestBody ToggleUserStatusRequest request) {
    User updatedUser = authenticationService.toggleUserStatus(request);

    String message = updatedUser.isActive()
        ? "Usuario activado exitosamente"
        : "Usuario desactivado exitosamente";

    return new Response(message,
        new EnableDisableUserResponseDTO(
            updatedUser.getId(),
            updatedUser.getName(),
            updatedUser.getUsername(),
            updatedUser.getEmail(),
            updatedUser.isActive()),
        HttpStatus.OK);
  }

  private AuthenticationProvider getAuthenticationProvider() {
    AuthenticationProvider provider = authProviderFactory.getProvider(activeAuthProvider);
    if (provider == null) {
      throw new AuthenticationFailedException("Proveedor de autenticación no configurado");
    }
    return provider;
  }

  private List<CorporationResponseDTO> mapCorporations(Set<Corporation> corporations) {
    return corporations.stream()
        .map(corp -> new CorporationResponseDTO(corp.getId(), corp.getName()))
        .toList();
  }

  private List<RoleResponseDTO> mapRoles(Set<Role> roles) {
    return roles.stream()
        .map(corp -> new RoleResponseDTO(corp.getId(), corp.getName(), corp.getDescription()))
        .toList();
  }
}
