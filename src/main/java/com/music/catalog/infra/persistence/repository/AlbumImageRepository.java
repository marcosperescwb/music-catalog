package com.music.catalog.infra.persistence.repository;

import com.music.catalog.infra.persistence.entity.AlbumImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumImageRepository extends JpaRepository<AlbumImageEntity, Long> {

    // Busca todas as imagens de um álbum específico
    List<AlbumImageEntity> findByAlbumId(Long albumId);
}