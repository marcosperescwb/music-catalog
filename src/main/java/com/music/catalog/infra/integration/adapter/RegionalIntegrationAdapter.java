package com.music.catalog.infra.integration.adapter;

import com.music.catalog.core.domain.Regional;
import com.music.catalog.core.port.out.RegionalIntegrationPort;
import com.music.catalog.infra.integration.dto.RegionalExternalDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegionalIntegrationAdapter implements RegionalIntegrationPort { // Ops, corrigirei o nome abaixo

    private final RestClient.Builder restClientBuilder;

    @Value("${app.integrations.regionals-url}")
    private String regionalsUrl;

    @Override
    public List<Regional> fetchAllRemote() {
        log.info("Fetching regionals from external API: {}", regionalsUrl);

        try {
            RestClient restClient = restClientBuilder.build();

            List<RegionalExternalDto> response = restClient.get()
                    .uri(regionalsUrl)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

            if (response == null) return Collections.emptyList();

            // Converte DTO Externo -> Domínio
            return response.stream()
                    .map(dto -> new Regional(null, dto.id(), dto.name(), true))
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching regionals", e);
            // Em caso de erro na integração, retornamos lista vazia para não quebrar o fluxo,
            // ou poderíamos lançar uma exceção de negócio dependendo da criticidade.
            // Para este teste, vamos assumir fail-safe (retorna vazio).
            return Collections.emptyList();
        }
    }
}