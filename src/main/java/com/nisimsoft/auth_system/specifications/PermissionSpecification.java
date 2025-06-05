package com.nisimsoft.auth_system.specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.nisimsoft.auth_system.entities.Permission;

public class PermissionSpecification {
    public static Specification<Permission> build(String searchWord, Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchWord != null && !searchWord.isBlank()) {
                String like = "%" + searchWord.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), like);
                Predicate valuePredicate = cb.like(cb.lower(root.get("value")), like);
                predicates.add(cb.or(namePredicate, valuePredicate));
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
