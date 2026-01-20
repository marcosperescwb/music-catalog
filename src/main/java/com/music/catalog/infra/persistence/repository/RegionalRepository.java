package com.music.catalog.infra.persistence.repository;

import com.music.catalog.infra.persistence.entity.RegionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionalRepository extends JpaRepository<RegionalEntity, Long> {

    // Busca apenas as versões ativas para comparação
    List<RegionalEntity> findByActiveTrue();
}