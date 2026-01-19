package com.music.catalog.infra.persistence.mapper;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.infra.persistence.entity.ArtistEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist toDomain(ArtistEntity entity);

    ArtistEntity toEntity(Artist domain);
}