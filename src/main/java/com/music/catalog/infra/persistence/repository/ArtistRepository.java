package com.music.catalog.infra.persistence.repository;

import com.music.catalog.infra.persistence.entity.ArtistEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {

    // Busca por nome (case insensitive) com paginação e ordenação dinâmica
    Page<ArtistEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Verifica duplicidade antes de criar
    boolean existsByNameIgnoreCase(String name);
}