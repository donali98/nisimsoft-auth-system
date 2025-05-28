package com.nisimsoft.auth_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ns_programs")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Program {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @EqualsAndHashCode.Include
  private Long id;

  @Size(min = 3, message = "El nombre del programa debe tener al menos 3 caracteres")
  @Size(max = 50, message = "El nombre del programa no puede exceder los 50 caracteres")
  private String name;

  @Column(nullable = true)
  @Size(min = 3, message = "La URI del programa debe tener al menos 3 caracteres")
  @Size(max = 50, message = "La URI del programa no puede exceder los 50 caracteres")
  private String uri;

  @Size(min = 3, message = "El ícono del programa debe tener al menos 3 caracteres")
  @Size(max = 50, message = "El ícono del programa no puede exceder los 50 caracteres")
  private String icon;

  private Boolean pinned = false;

  // Campo autoreferenciado, opcional
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", nullable = true)
  private Program parent;

  @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
  private Set<Program> children = new HashSet<>();

  @ManyToMany
  @JoinTable(
      name = "ns_program_role",
      joinColumns = @JoinColumn(name = "program_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  @JsonIgnoreProperties("roles") // Evita recursividad al serializar
  private Set<Role> roles = new HashSet<>();
}
