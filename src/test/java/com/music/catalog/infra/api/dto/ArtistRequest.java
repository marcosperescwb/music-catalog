package com.music.catalog.infra.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ArtistRequest(
        @NotBlank(message = "Name is required")
        String name
) {}