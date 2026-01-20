package com.music.catalog.core.port.out;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;
import java.util.List;

public interface AlbumRepositoryPort {
    Album save(Album album);
    PageDomain<Album> findAll(int page, int size);
    boolean existsByTitleAndArtistId(String title, Long artistId);

    void saveImage(Long albumId, String fileName, String contentType);
    List<String> findImagesByAlbumId(Long albumId);

}