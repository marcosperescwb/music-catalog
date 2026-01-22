package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.exception.BusinessException;
import com.music.catalog.core.port.in.AlbumUseCase;
import com.music.catalog.core.port.out.AlbumRepositoryPort;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import com.music.catalog.core.port.out.FileStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.music.catalog.infra.api.dto.AlbumNotificationDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumService implements AlbumUseCase {

    private final AlbumRepositoryPort albumRepository;
    private final ArtistRepositoryPort artistRepository;
    private final FileStoragePort fileStorage;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public Album create(Album album, List<Long> artistIds) {
        // 1. Validação
        List<Artist> artists = artistRepository.findAllByIds(artistIds);
        if (artists.size() != artistIds.size()) {
            throw new BusinessException("One or more artists not found");
        }

        // 2. Vinculação e Salvamento
        album.setArtists(artists);
        Album savedAlbum = albumRepository.save(album);

        // 3. Notificação WebSocket (Fire and Forget)
        notifyFrontend(savedAlbum);

        return savedAlbum;
    }

    private void notifyFrontend(Album album) {
        try {
            String message = "New album released: " + album.getTitle();
            AlbumNotificationDto notification = new AlbumNotificationDto(album.getId(), album.getTitle(), message);

            // Envia para quem estiver inscrito em /topic/albums
            messagingTemplate.convertAndSend("/topic/albums", notification);
        } catch (Exception e) {
            // Logamos o erro mas não falhamos a transação principal
            // (O álbum foi criado, se o socket falhar, paciência)
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void uploadCover(Long albumId, String originalFilename, InputStream content, String contentType) {
        // 1. Gera nome único (UUID + Extensão)
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storageFileName = UUID.randomUUID() + extension;

        // 2. Salva no MinIO
        fileStorage.save(storageFileName, content, contentType);

        // 3. Salva referência no Banco
        albumRepository.saveImage(albumId, storageFileName, contentType);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDomain<Album> list(int page, int size) {
        PageDomain<Album> pageResult = albumRepository.findAll(page, size);

        // Enriquecimento: Para cada álbum, busca as imagens e gera URLs assinadas
        pageResult.getContent().forEach(album -> {
            List<String> fileNames = albumRepository.findImagesByAlbumId(album.getId());
            List<String> urls = fileNames.stream()
                    .map(fileStorage::generatePresignedUrl)
                    .toList();
            album.setCoverImageUrls(urls);
        });

        return pageResult;
    }
}