package com.music.catalog.infra.api.controller;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.port.in.ArtistUseCase;
import com.music.catalog.infra.api.dto.ArtistRequest;
import com.music.catalog.infra.api.dto.ArtistResponse;
import com.music.catalog.infra.api.dto.PageResponse;
import com.music.catalog.infra.api.mapper.ArtistApiMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/artists")
@RequiredArgsConstructor
@Tag(name = "Artists", description = "Management of musical artists")
public class ArtistController {

    private final ArtistUseCase artistUseCase;
    private final ArtistApiMapper mapper;

    @PostMapping
    @Operation(summary = "Create a new artist")
    public ResponseEntity<ArtistResponse> create(@RequestBody @Valid ArtistRequest request) {
        Artist domain = mapper.toDomain(request);
        Artist created = artistUseCase.create(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "List artists with pagination and search")
    public ResponseEntity<PageResponse<ArtistResponse>> list(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        PageDomain<Artist> pageResult = artistUseCase.list(query, page, size, sortDir);
        return ResponseEntity.ok(mapper.toPageResponse(pageResult));
    }
}