package com.music.catalog.core.port.out;

import java.io.InputStream;

public interface FileStoragePort {

    // Salva o arquivo e retorna o nome gerado (ex: uuid.jpg)
    String save(String fileName, InputStream content, String contentType);

    // Remove o arquivo
    void delete(String fileName);

    // Gera URL temporária para visualização (Presigned URL)
    String generatePresignedUrl(String fileName);
}