package com.nisimsoft.auth_system.specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.nisimsoft.auth_system.entities.Corporation;

import jakarta.persistence.criteria.Predicate;

public class CorporationSpecification {
    public static Specification<Corporation> build(String searchWord, Map<String, Object> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchWord != null && !searchWord.isBlank()) {
                String like = "%" + searchWord.toLowerCase() + "%";
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), like);
                Predicate usernamePredicate = cb.like(cb.lower(root.get("dbName")), like);
                Predicate emailPredicate = cb.like(cb.lower(root.get("username")), like);
                Predicate hostPredicate = cb.like(cb.lower(root.get("host")), like);
                Predicate dbEnginePredicate = cb.like(cb.lower(root.get("dbEngine")), like);

                predicates
                        .add(cb.or(namePredicate, usernamePredicate, emailPredicate, hostPredicate, dbEnginePredicate));
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
