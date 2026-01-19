package com.music.catalog.core.port.out;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;

public interface AlbumRepositoryPort {
    Album save(Album album);
    PageDomain<Album> findAll(int page, int size);
    boolean existsByTitleAndArtistId(String title, Long artistId);
}