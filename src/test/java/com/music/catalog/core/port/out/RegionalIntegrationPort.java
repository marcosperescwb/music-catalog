package com.music.catalog.core.port.out;

import com.music.catalog.core.domain.Regional;
import java.util.List;

public interface RegionalIntegrationPort {
    List<Regional> fetchAllRemote();
}