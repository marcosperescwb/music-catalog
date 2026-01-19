package com.music.catalog.infra.persistence.mapper;

import com.music.catalog.core.domain.Album;
import com.music.catalog.infra.persistence.entity.AlbumEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public interface AlbumMapper {

    @Mapping(target = "artists", source = "artists")
    Album toDomain(AlbumEntity entity);

    @Mapping(target = "artists", source = "artists")
    AlbumEntity toEntity(Album domain);
}