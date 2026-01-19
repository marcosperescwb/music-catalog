package com.music.catalog.infra.api.mapper;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.infra.api.dto.AlbumRequest;
import com.music.catalog.infra.api.dto.AlbumResponse;
import com.music.catalog.infra.api.dto.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArtistApiMapper.class})
public interface AlbumApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artists", ignore = true) // Artistas são preenchidos via ID no Service
    @Mapping(target = "coverImageUrls", ignore = true)
    Album toDomain(AlbumRequest request);

    AlbumResponse toResponse(Album domain);

    PageResponse<AlbumResponse> toPageResponse(PageDomain<Album> pageDomain);
}