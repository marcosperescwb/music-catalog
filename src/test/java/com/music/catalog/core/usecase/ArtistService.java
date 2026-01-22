package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.exception.ResourceAlreadyExistsException;
import com.music.catalog.core.port.in.ArtistUseCase;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArtistService implements ArtistUseCase {

    private final ArtistRepositoryPort repository;

    @Override
    @Transactional
    public Artist create(Artist artist) {
        if (repository.existsByName(artist.getName())) {
            throw new ResourceAlreadyExistsException("Artist with name '" + artist.getName() + "' already exists");
        }
        return repository.save(artist);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDomain<Artist> list(String query, int page, int size, String sortDir) {
        if (query != null && !query.isBlank()) {
            return repository.searchByName(query, page, size, sortDir);
        }
        return repository.findAll(page, size, sortDir);
    }
}