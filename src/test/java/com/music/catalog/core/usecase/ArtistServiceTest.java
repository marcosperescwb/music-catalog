package com.music.catalog.core.usecase;

import com.music.catalog.core.domain.Artist;
import com.music.catalog.core.domain.PageDomain;
import com.music.catalog.core.exception.ResourceAlreadyExistsException;
import com.music.catalog.core.port.out.ArtistRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistServiceTest {

    @Mock
    private ArtistRepositoryPort repository;

    @InjectMocks
    private ArtistService service;

    @Test
    @DisplayName("Should create artist successfully when name is unique")
    void shouldCreateArtistSuccessfully() {
        // Arrange
        Artist input = new Artist(null, "Linkin Park");
        Artist saved = new Artist(1L, "Linkin Park");

        when(repository.existsByName("Linkin Park")).thenReturn(false);
        when(repository.save(input)).thenReturn(saved);

        // Act
        Artist result = service.create(input);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Linkin Park", result.getName());
        verify(repository).save(input);
    }

    @Test
    @DisplayName("Should throw exception when artist name already exists")
    void shouldThrowExceptionWhenArtistExists() {
        // Arrange
        Artist input = new Artist(null, "Metallica");
        when(repository.existsByName("Metallica")).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(input));

        // Garante que o save NUNCA foi chamado
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should return paged list of artists")
    void shouldReturnPagedList() {
        // Arrange
        PageDomain<Artist> mockPage = new PageDomain<>(List.of(new Artist(1L, "A")), 0, 10, 1, 1);
        when(repository.findAll(0, 10, "asc")).thenReturn(mockPage);

        // Act
        PageDomain<Artist> result = service.list(null, 0, 10, "asc");

        // Assert
        assertEquals(1, result.getContent().size());
        verify(repository).findAll(0, 10, "asc");
    }
}