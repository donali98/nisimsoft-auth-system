package com.nisimsoft.auth_system.specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.nisimsoft.auth_system.entities.Role;

import jakarta.persistence.criteria.Predicate;

public class RoleSpecification {
    public static Specification<Role> build(String searchWord, Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchWord != null && !searchWord.isBlank()) {
                String like = "%" + searchWord.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), like);
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), like);

                predicates.add(cb.or(namePredicate, descriptionPredicate));
            }

            if (filters != null) {
                filters.forEach((key, value) -> {
                    if (value != null && !value.toString().isBlank()) {
                        predicates.add(cb.equal(root.get(key), value));
                    }
                });
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
