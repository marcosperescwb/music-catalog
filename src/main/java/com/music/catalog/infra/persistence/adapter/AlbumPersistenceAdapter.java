package com.music.catalog.infra.persistence.adapter;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.port.out.AlbumRepositoryPort;
import com.music.catalog.infra.persistence.entity.AlbumEntity;
import com.music.catalog.infra.persistence.mapper.AlbumMapper;
import com.music.catalog.infra.persistence.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AlbumPersistenceAdapter implements AlbumRepositoryPort {

    private final AlbumRepository repository;
    private final AlbumMapper mapper;

    @Override
    public Album save(Album album) {
        AlbumEntity entity = mapper.toEntity(album);
        AlbumEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public PageDomain<Album> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("releaseYear").descending());
        // Usamos o método otimizado com JOIN FETCH que criamos no Repository
        Page<AlbumEntity> result = repository.findAllWithArtists(pageable);

        List<Album> content = result.getContent().stream()
                .map(mapper::toDomain)
                .toList();

        return new PageDomain<>(
                content,
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public boolean existsByTitleAndArtistId(String title, Long artistId) {
        // Implementação simples: verifica se existe algum álbum com esse título
        // contendo o artista na lista. Poderia ser otimizado com @Query se necessário.
        return repository.findAll().stream()
                .anyMatch(a -> a.getTitle().equalsIgnoreCase(title) &&
                        a.getArtists().stream().anyMatch(art -> art.getId().equals(artistId)));
    }
}