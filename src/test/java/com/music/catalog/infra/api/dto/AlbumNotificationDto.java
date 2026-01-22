package com.music.catalog.infra.api.dto;

public record AlbumNotificationDto(
        Long albumId,
        String title,
        String message
) {}