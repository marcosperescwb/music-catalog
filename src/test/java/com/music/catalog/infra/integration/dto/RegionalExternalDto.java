package com.music.catalog.infra.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

// Record é perfeito para DTOs de integração
public record RegionalExternalDto(
        Long id,

        @JsonProperty("nome") // Mapeia o campo "nome" do JSON para "name" do Java
        String name
) {}