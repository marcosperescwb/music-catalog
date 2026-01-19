package com.music.catalog.infra.api.controller;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.port.in.AlbumUseCase;
import com.music.catalog.infra.api.dto.AlbumRequest;
import com.music.catalog.infra.api.dto.AlbumResponse;
import com.music.catalog.infra.api.dto.PageResponse;
import com.music.catalog.infra.api.mapper.AlbumApiMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/albums")
@RequiredArgsConstructor
@Tag(name = "Albums", description = "Management of musical albums")
public class AlbumController {

    private final AlbumUseCase albumUseCase;
    private final AlbumApiMapper mapper;

    @PostMapping
    @Operation(summary = "Create a new album linked to artists")
    public ResponseEntity<AlbumResponse> create(@RequestBody @Valid AlbumRequest request) {
        Album domain = mapper.toDomain(request);
        // Passamos os IDs separadamente para o UseCase
        Album created = albumUseCase.create(domain, request.artistIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "List albums with pagination")
    public ResponseEntity<PageResponse<AlbumResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageDomain<Album> pageResult = albumUseCase.list(page, size);
        return ResponseEntity.ok(mapper.toPageResponse(pageResult));
    }
}