package com.music.catalog.infra.persistence.mapper;

import com.music.catalog.core.domain.Regional;
import com.music.catalog.infra.persistence.entity.RegionalEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionalMapper {
    Regional toDomain(RegionalEntity entity);
    RegionalEntity toEntity(Regional domain);
}