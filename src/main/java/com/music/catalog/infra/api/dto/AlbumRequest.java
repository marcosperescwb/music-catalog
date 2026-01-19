package com.music.catalog.infra.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AlbumRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Release year is required")
        Integer releaseYear,

        @NotEmpty(message = "At least one artist is required")
        List<Long> artistIds
) {}