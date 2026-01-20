package com.music.catalog.infra.api.controller;

import com.music.catalog.core.port.in.SyncRegionalsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/regionals")
@RequiredArgsConstructor
@Tag(name = "Regionals", description = "Synchronization of regional data")
public class RegionalController {

    private final SyncRegionalsUseCase syncUseCase;

    @PostMapping("/sync")
    @Operation(summary = "Trigger synchronization with external API")
    public ResponseEntity<Void> sync() {
        syncUseCase.execute();
        return ResponseEntity.noContent().build();
    }
}