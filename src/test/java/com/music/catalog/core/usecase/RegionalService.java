package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Regional;
import com.music.catalog.core.port.in.SyncRegionalsUseCase;
import com.music.catalog.core.port.out.RegionalIntegrationPort;
import com.music.catalog.core.port.out.RegionalRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionalService implements SyncRegionalsUseCase {

    private final RegionalRepositoryPort repository;
    private final RegionalIntegrationPort integration;

    @Override
    @Transactional
    public void execute() {
        log.info("Starting regionals synchronization...");

        // 1. Busca dados externos (Fonte da verdade)
        List<Regional> remotes = integration.fetchAllRemote();

        // 2. Busca dados locais ativos (Estado atual)
        List<Regional> locals = repository.findAllActive();

        // Otimização Sênior: Transformar lista em Map para busca O(1)
        // Chave: OriginalId, Valor: Objeto Regional
        Map<Long, Regional> localMap = locals.stream()
                .collect(Collectors.toMap(Regional::getOriginalId, Function.identity()));

        // 3. Processa Remotos (Inserções e Atualizações)
        for (Regional remote : remotes) {
            Regional local = localMap.get(remote.getOriginalId());

            if (local == null) {
                // CASO 1: Novo no endpoint -> Inserir
                insertNew(remote);
            } else {
                // Remove do mapa para marcar como processado (o que sobrar no mapa será inativado)
                localMap.remove(remote.getOriginalId());

                if (!local.getName().equals(remote.getName())) {
                    // CASO 3: Atributo alterado -> Inativar antigo e criar novo
                    log.info("Regional changed: {} -> {}", local.getName(), remote.getName());
                    repository.deactivate(local);
                    insertNew(remote);
                }
                // Se nome for igual, não faz nada (CASO 4: Mantém ativo)
            }
        }

        // 4. Processa Restantes Locais (Ausentes no endpoint)
        // CASO 2: Ausente no endpoint -> Inativar
        for (Regional missing : localMap.values()) {
            log.info("Regional missing in remote source, deactivating: {}", missing.getName());
            repository.deactivate(missing);
        }

        log.info("Synchronization finished.");
    }

    private void insertNew(Regional remote) {
        // Garante que o ID interno seja nulo para criar um novo registro
        Regional newRegional = new Regional(null, remote.getOriginalId(), remote.getName(), true);
        repository.save(newRegional);
    }
}