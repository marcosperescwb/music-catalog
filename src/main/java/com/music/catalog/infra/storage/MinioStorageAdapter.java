package com.music.catalog.infra.storage;

import com.music.catalog.core.port.out.FileStoragePort;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class MinioStorageAdapter implements FileStoragePort {

    private final MinioClient minioClient;

    @Value("${app.storage.bucket-name}")
    private String bucketName;

    @Override
    public String save(String fileName, InputStream content, String contentType) {
        try {
            // PutObject é o comando S3 para upload
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(content, -1, 10485760) // -1 = tamanho desconhecido, 10MB part size
                            .contentType(contentType)
                            .build()
            );
            return fileName;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO", e);
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error deleting file from MinIO", e);
            // Não lançamos erro aqui para não travar rollback de transação de banco
        }
    }

    @Override
    public String generatePresignedUrl(String fileName) {
        try {
            // Gera URL válida por 30 minutos (Requisito do projeto)
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(30, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error generating presigned URL", e);
            return null;
        }
    }
}