package com.music.catalog.infra.api.mapper;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.infra.api.dto.ArtistRequest;
import com.music.catalog.infra.api.dto.ArtistResponse;
import com.music.catalog.infra.api.dto.PageResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistApiMapper {

    @Mapping(target = "id", ignore = true)
    Artist toDomain(ArtistRequest request);

    ArtistResponse toResponse(Artist domain);

    PageResponse<ArtistResponse> toPageResponse(PageDomain<Artist> pageDomain);
}