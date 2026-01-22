-- Tabela de Artistas
CREATE TABLE artists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índice para busca rápida por nome (Requisito: Consulta por nome com ordenação)
CREATE INDEX idx_artists_name ON artists(name);

-- Tabela de Álbuns
CREATE TABLE albums (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    release_year INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela Associativa (Relacionamento N:N entre Artistas e Álbuns)
CREATE TABLE album_artists (
    album_id BIGINT NOT NULL,
    artist_id BIGINT NOT NULL,
    CONSTRAINT pk_album_artists PRIMARY KEY (album_id, artist_id),
    CONSTRAINT fk_album_artists_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE,
    CONSTRAINT fk_album_artists_artist FOREIGN KEY (artist_id) REFERENCES artists(id) ON DELETE CASCADE
);

-- Tabela de Imagens de Capa (Requisito: Uma ou mais imagens por álbum)
CREATE TABLE album_images (
    id BIGSERIAL PRIMARY KEY,
    album_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_album_images_album FOREIGN KEY (album_id) REFERENCES albums(id) ON DELETE CASCADE
);