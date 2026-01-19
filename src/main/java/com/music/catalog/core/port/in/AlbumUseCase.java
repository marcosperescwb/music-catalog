package com.music.catalog.core.port.in;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;
import java.util.List;

public interface AlbumUseCase {

    // Recebe o álbum e a lista de IDs dos artistas para vincular
    Album create(Album album, List<Long> artistIds);

    PageDomain<Album> list(int page, int size);
}