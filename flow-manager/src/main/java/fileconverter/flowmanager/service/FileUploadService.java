package fileconverter.flowmanager.service;

import fileconverter.flowmanager.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import fileconverter.flowmanager.exception.FileUploadException;
import fileconverter.flowmanager.model.ConversionJob;
import fileconverter.flowmanager.model.ConversionStatus;
import fileconverter.flowmanager.repository.ConversionJobRepository;
import fileconverter.flowmanager.event.FileSubmittedEvent;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileUploadService {

    private final MinioService minioService;
    private final ConversionJobRepository jobRepository;
    private final KafkaTemplate<String, FileSubmittedEvent> kafkaTemplate;

    @Value("${minio.bucket}")
    private String bucket;

    public String saveFileAndPublish(MultipartFile file, String userLogin, SubscriptionDto subscription) {
        if (file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        UUID jobId = UUID.randomUUID();
        String originalFilename = file.getOriginalFilename();
        String sanitizedFilename = originalFilename != null
                ? originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_")
                : "file_" + jobId;
        String minioInputPath = "input/" + jobId + "/" + sanitizedFilename;

        try {
            // Сохраняем файл в MinIO
            byte[] fileBytes = file.getBytes();
            minioService.save(fileBytes, minioInputPath, bucket);

            // Создаём запись в БД
            ConversionJob job = ConversionJob.builder()
                    .id(jobId)
                    .originalFileName(originalFilename)
                    .minioInputPath(minioInputPath)
                    .status(ConversionStatus.PROCESSING)
                    .build();
            jobRepository.save(job);

            // Отправляем событие в Kafka
            kafkaTemplate.send("file.submitted", jobId.toString(),
                    new FileSubmittedEvent(jobId.toString(), minioInputPath));

            log.info("File uploaded and event published for jobId: {}", jobId);
            return jobId.toString();

        } catch (IOException e) {
            log.error("Failed to read file content", e);
            throw new FileUploadException("Failed to read file", e);
        }
    }
}
