package com.music.catalog.core.port.out;

import com.music.catalog.core.domain.Regional;
import java.util.List;

public interface RegionalRepositoryPort {
    // Salva (ou atualiza) uma regional
    Regional save(Regional regional);

    // Busca todas que estão ativas no momento (para comparação)
    List<Regional> findAllActive();

    // Inativa uma lista de regionais (batch update é melhor, mas faremos simples primeiro)
    void deactivate(Regional regional);
}