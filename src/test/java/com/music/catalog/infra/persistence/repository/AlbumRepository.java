package com.music.catalog.infra.persistence.repository;

import com.music.catalog.infra.persistence.entity.AlbumEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumEntity, Long> {

    // Busca paginada padrão, mas otimizada para trazer os artistas juntos se necessário
    // O "LEFT JOIN FETCH" força o carregamento dos artistas na mesma query SQL
    @Query(value = "SELECT a FROM AlbumEntity a LEFT JOIN FETCH a.artists",
            countQuery = "SELECT count(a) FROM AlbumEntity a")
    Page<AlbumEntity> findAllWithArtists(Pageable pageable);
}