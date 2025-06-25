package com.nisimsoft.auth_system.services;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.nisimsoft.auth_system.entities.Corporation;
import com.nisimsoft.auth_system.repositories.CorporationRepository;
import com.nisimsoft.auth_system.specifications.CorporationSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CorporationService {
    private final CorporationRepository corporationRepository;

    public Page<Corporation> getCorporations(
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

        Specification<Corporation> spec = CorporationSpecification.build(searchWord, filters);

        return corporationRepository.findAll(spec, pageable);
    }
}
