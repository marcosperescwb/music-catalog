package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Album;
import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.exception.BusinessException;
import com.music.catalog.core.port.out.AlbumRepositoryPort;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import com.music.catalog.core.port.out.FileStoragePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock private AlbumRepositoryPort albumRepository;
    @Mock private ArtistRepositoryPort artistRepository;
    @Mock private FileStoragePort fileStorage;
    @Mock private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private AlbumService service;

    @Test
    @DisplayName("Should create album successfully when all artists exist")
    void shouldCreateAlbumSuccessfully() {
        // Arrange
        Album input = new Album(null, "Hybrid Theory", 2000, null, null);
        List<Long> artistIds = List.of(1L);
        List<Artist> foundArtists = List.of(new Artist(1L, "Linkin Park"));
        Album savedAlbum = new Album(10L, "Hybrid Theory", 2000, foundArtists, null);

        when(artistRepository.findAllByIds(artistIds)).thenReturn(foundArtists);
        when(albumRepository.save(input)).thenReturn(savedAlbum);

        // Act
        Album result = service.create(input, artistIds);

        // Assert
        assertNotNull(result.getId());
        assertEquals(1, result.getArtists().size());
        verify(messagingTemplate).convertAndSend(anyString(), any(Object.class)); // Verifica se notificou
    }

    @Test
    @DisplayName("Should throw exception when one or more artists are missing")
    void shouldThrowExceptionWhenArtistMissing() {
        // Arrange
        Album input = new Album(null, "Lost Album", 2020, null, null);
        List<Long> artistIds = List.of(1L, 99L); // ID 99 não existe
        List<Artist> foundArtists = List.of(new Artist(1L, "Existing Artist")); // Só achou 1

        when(artistRepository.findAllByIds(artistIds)).thenReturn(foundArtists);

        // Act & Assert
        BusinessException ex = assertThrows(BusinessException.class, () -> service.create(input, artistIds));
        assertEquals("One or more artists not found", ex.getMessage());

        verify(albumRepository, never()).save(any());
    }
}