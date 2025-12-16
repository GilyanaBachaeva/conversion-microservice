package spring_micro.pdf_converter.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring_micro.pdf_converter.exception.MinioStorageException;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public byte[] download(String objectName) {
        try {
            GetObjectArgs args = GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(objectName)
                    .build();
            try (var stream = minioClient.getObject(args)) {
                return stream.readAllBytes();
            }
        } catch (Exception e) {
            throw new MinioStorageException("Download failed: " + objectName, e);
        }
    }

    public void upload(byte[] data, String objectName) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .build()
            );
        } catch (Exception e) {
            throw new MinioStorageException("Upload failed: " + objectName, e);
        }
    }
}
