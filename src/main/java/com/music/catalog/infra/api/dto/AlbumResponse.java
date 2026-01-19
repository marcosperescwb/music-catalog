package com.music.catalog.infra.api.dto;

import java.util.List;

public record AlbumResponse(
        Long id,
        String title,
        Integer releaseYear,
        List<ArtistResponse> artists
) {}