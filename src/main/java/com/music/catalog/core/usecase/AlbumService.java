package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.exception.BusinessException;
import com.music.catalog.core.port.in.AlbumUseCase;
import com.music.catalog.core.port.out.AlbumRepositoryPort;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService implements AlbumUseCase {

    private final AlbumRepositoryPort albumRepository;
    private final ArtistRepositoryPort artistRepository;

    @Override
    @Transactional
    public Album create(Album album, List<Long> artistIds) {
        // 1. Validação: Busca os artistas pelos IDs informados
        List<Artist> artists = artistRepository.findAllByIds(artistIds);

        // 2. Regra de Negócio: Todos os IDs informados devem existir
        if (artists.size() != artistIds.size()) {
            throw new BusinessException("One or more artists not found");
        }

        // 3. Vinculação
        album.setArtists(artists);

        return albumRepository.save(album);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDomain<Album> list(int page, int size) {
        return albumRepository.findAll(page, size);
    }
}