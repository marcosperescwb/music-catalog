package com.music.catalog.core.port.in;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;

public interface ArtistUseCase {

    Artist create(Artist artist);

    PageDomain<Artist> list(String query, int page, int size, String sortDir);
}