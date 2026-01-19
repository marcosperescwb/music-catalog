package com.music.catalog.infra.persistence.adapter;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import com.music.catalog.infra.persistence.entity.ArtistEntity;
import com.music.catalog.infra.persistence.mapper.ArtistMapper;
import com.music.catalog.infra.persistence.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArtistPersistenceAdapter implements ArtistRepositoryPort {

    private final ArtistRepository repository;
    private final ArtistMapper mapper;

    @Override
    public Artist save(Artist artist) {
        ArtistEntity entity = mapper.toEntity(artist);
        ArtistEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public PageDomain<Artist> findAll(int page, int size, String sortDir) {
        Pageable pageable = createPageable(page, size, sortDir);
        Page<ArtistEntity> result = repository.findAll(pageable);
        return convertToPageDomain(result);
    }

    @Override
    public PageDomain<Artist> searchByName(String name, int page, int size, String sortDir) {
        Pageable pageable = createPageable(page, size, sortDir);
        Page<ArtistEntity> result = repository.findByNameContainingIgnoreCase(name, pageable);
        return convertToPageDomain(result);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    // Métodos Auxiliares Privados

    private Pageable createPageable(int page, int size, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, "name"));
    }

    private PageDomain<Artist> convertToPageDomain(Page<ArtistEntity> pageEntity) {
        List<Artist> content = pageEntity.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PageDomain<>(
                content,
                pageEntity.getNumber(),
                pageEntity.getSize(),
                pageEntity.getTotalElements(),
                pageEntity.getTotalPages()
        );
    }
}