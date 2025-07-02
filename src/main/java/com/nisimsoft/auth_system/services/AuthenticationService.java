package com.nisimsoft.auth_system.services;

import com.nisimsoft.auth_system.dtos.requests.AssignRolesToUserRequest;
import com.nisimsoft.auth_system.dtos.requests.SaveOrUpdateUserRequest;
import com.nisimsoft.auth_system.dtos.requests.ToggleUserStatusRequest;
import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.entities.Role;
import com.nisimsoft.auth_system.entities.User;
import com.nisimsoft.auth_system.exceptions.auth.AuthenticationFailedException;
import com.nisimsoft.auth_system.repositories.CorporationRepository;
import com.nisimsoft.auth_system.repositories.RoleRepository;
import com.nisimsoft.auth_system.repositories.UserRepository;
import com.nisimsoft.auth_system.services.providers.AuthenticationProvider;
import com.nisimsoft.auth_system.specifications.UserSpecification;

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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final CorporationRepository corporationRepository;
  private final RoleRepository roleRepository;

  @Transactional
  public User saveOrUpdateUser(SaveOrUpdateUserRequest request) {

    Long userId = request.getId();

    if (request.getPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
      throw new IllegalArgumentException("Las contraseñas no coinciden");
    }

    User user = userId != null
        ? userRepository.findById(userId).orElseGet(User::new)
        : new User();

    user.setName(request.getName());
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());

    if (request.getPassword() != null)
      user.setPassword(passwordEncoder.encode(request.getPassword()));

    List<Corporation> foundCorps = corporationRepository.findAllById(request.getCorporationIds());
    List<Role> foundRoles = roleRepository.findAllById(request.getRoleIds());

    if (foundCorps.size() != request.getCorporationIds().size()) {
      throw new IllegalArgumentException("Una o más corporaciones no existen en el sistema");
    }

    if (foundRoles.size() != request.getRoleIds().size()) {
      throw new IllegalArgumentException("Uno o más roles no existen en el sistema");
    }

    user.setCorporations(new HashSet<>(foundCorps));
    user.setRoles(new HashSet<>(foundRoles));

    return userRepository.save(user);
  }

  public Page<User> getUsers(
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

    Specification<User> spec = UserSpecification.build(searchWord, filters);

    return userRepository.findAll(spec, pageable);
  }

  @Transactional
  public User toggleUserStatus(ToggleUserStatusRequest request) {
    User targetUser = getUserByIdOrThrow(request.getId());
    User currentUser = getCurrentAuthenticatedUser();

    if (targetUser.getId().equals(currentUser.getId())) {
      throw new IllegalArgumentException("No puedes deshabilitar tu propio usuario.");
    }

    targetUser.setActive(request.getIsActive());
    return userRepository.save(targetUser);
  }

  public User getCurrentAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("No hay usuario autenticado en el contexto");
    }

    String email = (String) authentication.getPrincipal(); // <- porque tú pusiste el email como principal

    return getUserByEmailOrThrow(email);
  }

  public User getUserByEmailOrThrow(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }

  public void authenticateOrThrow(AuthenticationProvider provider, String email, String password) {
    if (!provider.authenticate(email, password)) {
      throw new AuthenticationFailedException("Credenciales inválidas");
    }
  }

  public void validateUserBelongsToCorporation(User user, Long corpId) {
    boolean belongs = user.getCorporations().stream().anyMatch(c -> c.getId().equals(corpId));
    if (!belongs) {
      throw new AuthenticationFailedException("El usuario no pertenece a la corporación");
    }
  }

  public User getUserByIdOrThrow(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
  }

  @Transactional
  public User assignRoleToUser(AssignRolesToUserRequest request) {

    User user = userRepository
        .findById(request.getId())
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado")); // Si no existe

    List<Long> roleIds = request.getRoles();
    // Buscar los permisos por ID
    List<Role> foundRoles = roleRepository.findAllById(roleIds);

    if (foundRoles.size() != request.getRoles().size()) {
      throw new IllegalArgumentException("Uno o más roles no existen en el sistema");
    }

    // Actualizar el set de permisos
    user.setRoles(new HashSet<>(foundRoles));

    return userRepository.save(user);
  }
}
