package com.music.catalog.infra.persistence.adapter;

import com.music.catalog.core.domain.Regional;
import com.music.catalog.core.port.out.RegionalRepositoryPort;
import com.music.catalog.infra.persistence.entity.RegionalEntity;
import com.music.catalog.infra.persistence.mapper.RegionalMapper;
import com.music.catalog.infra.persistence.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionalPersistenceAdapter implements RegionalRepositoryPort {

    private final RegionalRepository repository;
    private final RegionalMapper mapper;

    @Override
    public Regional save(Regional regional) {
        RegionalEntity entity = mapper.toEntity(regional);
        RegionalEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<Regional> findAllActive() {
        return repository.findByActiveTrue().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deactivate(Regional regional) {
        // Para inativar, precisamos do ID interno (PK)
        // Se o objeto de domínio já tiver o ID, usamos ele.
        if (regional.getId() != null) {
            RegionalEntity entity = repository.findById(regional.getId())
                    .orElseThrow(() -> new RuntimeException("Regional not found"));

            entity.setActive(false);
            repository.save(entity);
        }
    }
}