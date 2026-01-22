package com.music.catalog.core.port.out;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import java.util.List;

public interface ArtistRepositoryPort {
    Artist save(Artist artist);
    PageDomain<Artist> findAll(int page, int size, String sortDir);
    PageDomain<Artist> searchByName(String name, int page, int size, String sortDir);
    boolean existsByName(String name);

    List<Artist> findAllByIds(List<Long> ids);
}

